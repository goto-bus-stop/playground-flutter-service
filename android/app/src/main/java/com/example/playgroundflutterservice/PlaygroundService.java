package com.example.playgroundflutterservice;

import android.os.Binder;
import android.os.IBinder;
import android.app.Service;
import android.app.NotificationManager;
import android.app.Notification;
import android.content.Intent;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.util.Timer;
import java.util.TimerTask;

public class PlaygroundService extends Service {
  private static final int NOTIFY_ID = 1312; // the good kind of dogwhistle
  private static EventChannel events;
  private int value = 0;
  private NotificationManager manager;
  private Notification.Builder notification;
  private Timer timer;
  private TimerTask task;
  private EventChannel.EventSink sink;

  public static void registerWith(Registrar registrar) {
    MethodChannel channel = new MethodChannel(registrar.messenger(), "example");
    channel.setMethodCallHandler((methodCall, result) -> {
      try {
        registrar.activity()
          .startService(new Intent(registrar.activity(), PlaygroundService.class));
        result.success(null);
      } catch (Exception err) {
        result.error("Exception", err.getMessage(), null);
        err.printStackTrace();
      }
    });
    events = new EventChannel(registrar.messenger(), "events");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    System.out.println("onCreate");

    events.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onCancel(Object args) {
        System.out.println("streamHandler onCancel");
        sink = null;
      }

      @Override
      public void onListen(Object args, EventChannel.EventSink events) {
        sink = events;
        System.out.println("streamHandler onListen");
      }
    });

    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    timer = new Timer();
    notification = new Notification.Builder(this)
      .setSmallIcon(android.R.drawable.ic_media_play)
      .setOngoing(true)
      .setContentTitle("foreground service");

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    System.out.println("onStartCommand");
    if (task != null) {
      increment();
      return START_NOT_STICKY;
    }

    task = new TimerTask() {
      @Override public void run() {
        value++;
        notification.setContentText("value = " + value);
        manager.notify(NOTIFY_ID, notification.build());
      }
    };

    timer.schedule(task, 1000, 7000);

    notification.setContentText("value = " + value);
    startForeground(NOTIFY_ID, notification.build());

    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    System.out.println("onDestroy");
    timer.cancel();
    timer = null;
    manager = null;

    super.onDestroy();
  }

  public void increment() {
    value++;
    notification.setContentText("value = " + value);
    manager.notify(NOTIFY_ID, notification.build());

    if (sink != null) {
      sink.success(value);
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    System.out.println("onBind");
    return new PBinder();
  }

  public class PBinder extends Binder {
    PlaygroundService getService() {
      return PlaygroundService.this;
    }
  }
}

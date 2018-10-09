package com.example.playgroundflutterservice;

import android.os.Bundle;
import android.content.Intent;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
  private MethodChannel channel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    channel = new MethodChannel(registrarFor(PlaygroundService.class.getName()).messenger(), "example");
    channel.setMethodCallHandler((methodCall, result) -> {
      try {
        startService(new Intent(this, PlaygroundService.class));
        result.success(null);
      } catch (Exception err) {
        result.error("Exception", err.getMessage(), null);
        err.printStackTrace();
      }
    });
  }
}

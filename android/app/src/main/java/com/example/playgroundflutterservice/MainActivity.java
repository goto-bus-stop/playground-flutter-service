package com.example.playgroundflutterservice;

import android.os.Bundle;
import android.content.Intent;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    PlaygroundService.registerWith(registrarFor(PlaygroundService.class.getName()));
  }
}

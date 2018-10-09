# playground_flutter_service

Trying to make a Dart WebSocket run in an Android foreground service.

## Goals

1. Create a Service in a Flutter plugin.
1. Call into Dart from a Service.
1. Create a WebSocket connection in Dart, inside a Service.
1. Use a MethodChannel to display incoming messages in a notification.
1. Keep some state in Dart that is shown when the UI opens.

Any of those may be impossible at this stage, I don't know!
It likely involves learning about some Dart VM stuff like isolates, and some Android stuff like…Services, to start with…

I'm only interested in doing this for Android right now because I don't own any Apple devices.

## License

[Apache-2.0](./LICENSE.md)

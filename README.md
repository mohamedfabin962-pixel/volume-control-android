# Volume Control Android

A lightweight native Android utility that provides quick media volume control when the physical volume button is unavailable or inconvenient to use.

## Features

- Floating edge handle
- Works over other apps
- Quick access to media volume control
- Custom vertical volume slider
- Real-time media volume adjustment
- Automatically hides the volume slider after inactivity
- No backend, database, account, or internet connection required

## How It Works

The app runs a lightweight background service that displays a floating handle on the edge of the screen.

When the handle is tapped:

1. A vertical volume slider appears.
2. The current media volume is displayed.
3. Dragging the slider changes the device's media volume using Android's `AudioManager`.
4. The slider automatically disappears after a short period of inactivity.

The app uses Android's overlay functionality so the volume control can be accessed while using other applications.

## Tech Stack

- Kotlin
- Android
- Jetpack Compose
- Android WindowManager
- Android AudioManager
- Custom Android View

## Current Status

✅ Version 1 — Working prototype

The app has been tested on an Android emulator and a Huawei Android 9 device.

### Known Limitation

On some devices, including the tested Huawei Android 9 device, the floating handle does not automatically return after a full device power-off and reboot.

Opening the Volume Control app once after reboot starts the service and restores the floating handle.

## Planned Improvements

- Better floating handle design
- Improved transparency and positioning
- Permission handling improvements
- Tap feedback
- Additional Android-device compatibility improvements
- Support for additional volume streams such as ringtone volume

## Author

Mohamed Fabin

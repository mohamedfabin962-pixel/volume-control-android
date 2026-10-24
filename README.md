# Volume Control Android

A lightweight native Android utility that provides quick media volume control when the physical volume button is unavailable or inconvenient to use.

## Features

- Floating edge handle
- Works over other apps
- Quick access to media volume control
- Custom vertical volume slider
- Real-time media volume adjustment
- Automatically hides the volume slider after inactivity
- Floating handle automatically starts after device reboot
- No backend, database, account, or internet connection required

## Tech Stack

- Kotlin
- Android
- Jetpack Compose
- Android WindowManager
- Android AudioManager
- Custom Android View

## How It Works

The app runs a lightweight background service that displays a floating handle on the edge of the screen.

When the handle is tapped:

1. A vertical volume slider appears.
2. The current media volume is displayed.
3. Dragging the slider changes the device's media volume using Android's `AudioManager`.
4. The slider automatically disappears after a short period of inactivity.

The app uses Android's overlay functionality so the volume control can be accessed while using other applications.

## Current Status

✅ Version 1 — Working prototype

Tested on an Android emulator and Huawei Android 9 device.

## Planned Improvements

- Better floating handle design
- Improved transparency and positioning
- Permission handling improvements
- Tap feedback
- Additional Android-device compatibility improvements

## Author

Mohamed Fabin

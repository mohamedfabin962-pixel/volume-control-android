# Volume Control Android

A lightweight native Android utility that provides quick system-wide media volume control when the physical volume button is unavailable or inconvenient to use.

## Features

- Floating edge handle
- Works over other apps
- Quick access to media volume control
- Custom vertical volume slider
- Real-time media volume adjustment
- Automatically hides the volume slider after inactivity
- Long press opens Android Sound & Vibration settings
- Clear overlay permission flow
- Foreground service for reliable background operation
- No backend, database, account, or internet connection required

## How It Works

The app runs a foreground service that displays a floating handle on the edge of the screen using Android's overlay functionality.

### Startup & Permission Flow

The app does not use a traditional home screen. The floating controller itself is the primary interface.

When the app is opened:

1. If overlay permission is already granted, the floating controller starts immediately.
2. If permission has not been granted, the app first explains why the permission is required.
3. Selecting **Allow** opens Android's **Display over other apps** settings.
4. After the permission is enabled, returning to the app automatically starts the floating controller.
5. Selecting **Not Now**, or returning without granting the permission, closes the app.

### Volume Control

When the floating handle is tapped:

1. A vertical volume slider appears.
2. The current media volume is displayed.
3. Dragging the slider changes the device's media volume using Android's `AudioManager`.
4. The slider automatically disappears after a short period of inactivity.

When the handle is long-pressed:

1. Android's Sound & Vibration settings are opened.
2. The user can access the device's other sound controls from the system settings.

The floating control uses Android's `WindowManager` and application overlay functionality, allowing it to remain accessible while using other applications.

The overlay runs as a foreground service with a persistent notification. This helps prevent the Android system or device-specific background management from stopping the service while the device is in use.

## Technical Architecture

```text
Floating Handle
       ↓
WindowManager Overlay
       ↓
VolumeOverlayService
       ↓
AudioManager
       ↓
Android Media Volume
```

The service is implemented as a foreground service to improve reliability during screen-off/screen-on cycles.

## Tech Stack

- Kotlin
- Android
- Jetpack Compose
- Android WindowManager
- Android AudioManager
- Android Foreground Service
- Custom Android View

## Permissions & Android APIs

The application uses:

- `SYSTEM_ALERT_WINDOW` — to display the floating volume control over other apps
- `FOREGROUND_SERVICE` — to run the volume overlay as a foreground service
- `FOREGROUND_SERVICE_SPECIAL_USE` — for the special-use foreground service type
- `RECEIVE_BOOT_COMPLETED` — to support starting the service after device boot where permitted by the device

Key Android APIs:

- `WindowManager`
- `AudioManager`
- `Service`
- `NotificationChannel`
- `NotificationCompat`

## Current Status

✅ **Version 1.0.1 — Working**

The application has been tested on:

- Android emulator
- Huawei Android 9 real device

The foreground-service implementation has been tested through repeated screen-off/screen-on cycles on the Huawei device, and the floating handle remained available without requiring the application to be reopened.

### Known Limitation

On some devices, including the tested Huawei Android 9 device, the floating handle does not automatically return after a full device power-off and reboot.

Opening the Volume Control app once after reboot starts the service and restores the floating handle.

## Engineering Challenges

During development, an initial approach using Android accessibility/touch interception was tested for detecting edge gestures.

However, intercepting touchscreen events interfered with normal Android interactions such as:

- Back navigation
- Notification shade
- Touch interaction with other applications

The approach was therefore abandoned in favor of an application overlay.

A second issue occurred when the background service was destroyed after screen-off/screen-on cycles. Android service diagnostics using ADB and `dumpsys` showed that `VolumeOverlayService` was being destroyed.

The service was subsequently converted to a foreground service with a persistent notification, which resolved the screen-off/screen-on reliability issue observed during testing on the Huawei device.

## Planned Improvements

- Better floating handle design
- Improved transparency and positioning
- Tap feedback
- Additional Android-device compatibility improvements
- Support for additional volume streams such as ringtone and alarm volume
- Improved boot/restart behavior across device manufacturers

## 📖 Project Story

I wrote a detailed case study about building this project, including the problems I encountered with `AccessibilityService`, how I switched to a `WindowManager` overlay, how I diagnosed the service being destroyed after screen-off, and how a foreground service solved the issue.

**Read the full story on Medium:**  
https://medium.com/@mohamedfabin962/i-built-a-floating-android-volume-controller-when-my-phones-volume-button-stopped-working-9cdf73c0e09a

## 📥 Download

Want to try the app on an Android device?

[**Download Volume Control v1.0.1**](https://github.com/mohamedfabin962-pixel/volume-control-android/releases/tag/v1.0.1)

The release includes the APK for testing and demonstration.

## Author

Mohamed Fabin

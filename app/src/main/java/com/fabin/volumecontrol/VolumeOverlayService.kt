package com.fabin.volumecontrol

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button

class VolumeOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var button: View


    override fun onCreate() {
        super.onCreate()

        // Check overlay permission
        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        // Get WindowManager
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Create floating handle
        button = Button(this).apply {
            text = ""
            setBackgroundColor(Color.argb(80, 128, 128, 128))

            setOnClickListener {
                val intent = Intent(Settings.Panel.ACTION_VOLUME)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        // Position and size of floating handle
        val params = WindowManager.LayoutParams(
            60,
            180,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.END
        params.y = 500

        // Show floating handle
        windowManager.addView(button, params)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::button.isInitialized) {
            windowManager.removeView(button)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
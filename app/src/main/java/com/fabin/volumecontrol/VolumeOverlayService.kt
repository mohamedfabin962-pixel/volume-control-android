package com.fabin.volumecontrol

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.app.NotificationCompat

class VolumeOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var button: View
    private var slider: View? = null

    private val hideHandler = Handler(Looper.getMainLooper())

    private val hideSliderRunnable = Runnable {
        slider?.let {
            windowManager.removeView(it)
            slider = null
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Notification channels are supported from Android 8.0 (API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "volume_control",
                "Volume Control",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager =
                getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(
            this,
            "volume_control"
        )
            .setContentTitle("Volume Control")
            .setContentText("Volume control is active")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)

        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return
        }

        windowManager =
            getSystemService(WINDOW_SERVICE) as WindowManager

        button = Button(this).apply {

            text = ""

            setBackgroundColor(
                Color.argb(80, 128, 128, 128)
            )

            // Single tap → Media volume slider
            setOnClickListener {

                if (slider != null) {
                    return@setOnClickListener
                }

                val audioManager =
                    getSystemService(AUDIO_SERVICE) as AudioManager

                slider = VerticalVolumeView(
                    this@VolumeOverlayService,
                    audioManager.getStreamMaxVolume(
                        AudioManager.STREAM_MUSIC
                    ),
                    audioManager.getStreamVolume(
                        AudioManager.STREAM_MUSIC
                    )
                ) { volume ->

                    audioManager.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        volume,
                        0
                    )

                    hideHandler.removeCallbacks(
                        hideSliderRunnable
                    )

                    hideHandler.postDelayed(
                        hideSliderRunnable,
                        2000
                    )
                }

                val sliderParams = WindowManager.LayoutParams(
                    100,
                    320,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
                )

                sliderParams.gravity =
                    Gravity.TOP or Gravity.END

                sliderParams.x = 40
                sliderParams.y = 400

                windowManager.addView(
                    slider,
                    sliderParams
                )

                hideHandler.removeCallbacks(
                    hideSliderRunnable
                )

                hideHandler.postDelayed(
                    hideSliderRunnable,
                    2000
                )
            }

            // Long press → Android Sound & Vibration settings
            setOnLongClickListener {

                val intent = Intent(
                    Settings.ACTION_SOUND_SETTINGS
                )

                intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                )

                startActivity(intent)

                true
            }
        }

        val params = WindowManager.LayoutParams(
            60,
            180,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity =
            Gravity.TOP or Gravity.END

        params.y = 500

        windowManager.addView(
            button,
            params
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        hideHandler.removeCallbacks(
            hideSliderRunnable
        )

        if (::button.isInitialized) {
            windowManager.removeView(button)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
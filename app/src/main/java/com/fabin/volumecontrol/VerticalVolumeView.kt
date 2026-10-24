package com.fabin.volumecontrol

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class VerticalVolumeView(
    context: Context,
    private val maxVolume: Int,
    private var currentVolume: Int,
    private val onVolumeChanged: (Int) -> Unit
) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val trackWidth = 12f
    private val thumbRadius = 18f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f

        val top = 30f
        val bottom = height - 30f

        // Background track
        paint.color = 0x55333333
        val track = RectF(
            centerX - trackWidth / 2,
            top,
            centerX + trackWidth / 2,
            bottom
        )

        canvas.drawRoundRect(
            track,
            trackWidth / 2,
            trackWidth / 2,
            paint
        )

        // Current volume
        val volumeRatio =
            if (maxVolume > 0) {
                currentVolume.toFloat() / maxVolume
            } else {
                0f
            }

        val thumbY =
            bottom - ((bottom - top) * volumeRatio)

        // Active part
        paint.color = 0xFF777777.toInt()

        val activeTrack = RectF(
            centerX - trackWidth / 2,
            thumbY,
            centerX + trackWidth / 2,
            bottom
        )

        canvas.drawRoundRect(
            activeTrack,
            trackWidth / 2,
            trackWidth / 2,
            paint
        )

        // Thumb
        paint.color = 0xFFFFFFFF.toInt()

        canvas.drawCircle(
            centerX,
            thumbY,
            thumbRadius,
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {

                val top = 30f
                val bottom = height - 30f

                val y = min(
                    bottom,
                    max(top, event.y)
                )

                val ratio =
                    1f - ((y - top) / (bottom - top))

                currentVolume =
                    (ratio * maxVolume)
                        .toInt()
                        .coerceIn(0, maxVolume)

                onVolumeChanged(currentVolume)

                invalidate()

                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }
        }

        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
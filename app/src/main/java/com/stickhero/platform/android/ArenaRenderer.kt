package com.stickhero.platform.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class ArenaRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, width: Float, height: Float) {
        paint.style = Paint.Style.FILL
        paint.color = Color.rgb(174, 192, 158)
        canvas.drawRect(0f, height * 0.72f, width, height, paint)
        paint.color = Color.rgb(123, 145, 125)
        for (i in 0..7) {
            val x = i * width / 7f
            canvas.drawOval(RectF(x - 90f, height * 0.69f, x + 110f, height * 0.77f), paint)
        }
        paint.color = Color.argb(70, 255, 255, 255)
        canvas.drawCircle(width * 0.84f, height * 0.17f, 54f, paint)
        paint.color = Color.argb(60, 71, 102, 126)
        canvas.drawRect(0f, height * 0.54f, width, height * 0.72f, paint)
        paint.color = Color.rgb(77, 91, 72)
        paint.strokeWidth = 5f
        canvas.drawLine(0f, height * 0.72f, width, height * 0.72f, paint)
        paint.strokeWidth = 2f
        paint.color = Color.argb(80, 39, 55, 70)
        for (i in 0..12) {
            val x = i * width / 12f
            canvas.drawLine(x, height * 0.72f, x - 80f, height, paint)
        }
    }
}

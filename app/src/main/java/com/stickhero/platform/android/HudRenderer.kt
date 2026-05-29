package com.stickhero.platform.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.stickhero.game.renderstate.RenderSnapshot

class HudRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, snapshot: RenderSnapshot, width: Float) {
        val barWidth = width * 0.34f
        val barHeight = 24f
        drawBar(canvas, 24f, 24f, barWidth, barHeight, snapshot.hud.playerHealthFraction, Color.rgb(39, 174, 96))
        drawBar(canvas, width - 24f - barWidth, 24f, barWidth, barHeight, snapshot.hud.enemyHealthFraction, Color.rgb(231, 76, 60))
    }

    private fun drawBar(canvas: Canvas, x: Float, y: Float, w: Float, h: Float, fraction: Float, color: Int) {
        paint.style = Paint.Style.FILL
        paint.color = Color.rgb(42, 48, 55)
        canvas.drawRoundRect(RectF(x, y, x + w, y + h), 4f, 4f, paint)
        paint.color = color
        canvas.drawRoundRect(RectF(x + 3f, y + 3f, x + 3f + (w - 6f) * fraction, y + h - 3f), 3f, 3f, paint)
    }
}

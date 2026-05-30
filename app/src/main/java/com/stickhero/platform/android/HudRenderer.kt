package com.stickhero.platform.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.stickhero.game.core.GameMode
import com.stickhero.game.renderstate.RenderSnapshot

class HudRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, snapshot: RenderSnapshot, width: Float) {
        val barWidth = width * 0.34f
        val barHeight = 24f
        drawBar(canvas, 24f, 24f, barWidth, barHeight, snapshot.hud.playerHealthFraction, Color.rgb(39, 174, 96))
        if (snapshot.hud.mode == GameMode.Normal && snapshot.hud.enemyHealthFraction != null) {
            drawBar(canvas, width - 24f - barWidth, 24f, barWidth, barHeight, snapshot.hud.enemyHealthFraction, Color.rgb(231, 76, 60))
        }
        if (snapshot.hud.mode == GameMode.DebugSandbox) {
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.RIGHT
            paint.textSize = 18f
            paint.color = Color.argb(210, 18, 24, 30)
            canvas.drawText("DEBUG", width - 24f, 68f, paint)
        }
    }

    private fun drawBar(canvas: Canvas, x: Float, y: Float, w: Float, h: Float, fraction: Float, color: Int) {
        paint.style = Paint.Style.FILL
        paint.color = Color.rgb(42, 48, 55)
        canvas.drawRoundRect(RectF(x, y, x + w, y + h), 4f, 4f, paint)
        paint.color = color
        canvas.drawRoundRect(RectF(x + 3f, y + 3f, x + 3f + (w - 6f) * fraction, y + h - 3f), 3f, 3f, paint)
    }
}

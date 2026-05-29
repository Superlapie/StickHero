package com.stickhero.platform.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.stickhero.game.renderstate.ImpactEffectRenderModel
import kotlin.math.cos
import kotlin.math.sin

class ImpactEffectRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, effects: List<ImpactEffectRenderModel>) {
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        effects.forEach { effect ->
            val alpha = ((1f - effect.progress) * 220f).toInt().coerceIn(0, 220)
            val radius = (12f + effect.progress * 34f) * effect.strength
            paint.color = Color.argb(alpha, 255, 239, 154)
            paint.strokeWidth = 4f * effect.strength
            for (i in 0 until 7) {
                val angle = i * 0.897f + effect.progress * 0.4f
                val start = radius * 0.35f
                val end = radius
                val sx = effect.position.x + cos(angle) * start
                val sy = effect.position.y + sin(angle) * start
                val ex = effect.position.x + cos(angle) * end
                val ey = effect.position.y + sin(angle) * end
                canvas.drawLine(sx, sy, ex, ey, paint)
            }
        }
    }
}

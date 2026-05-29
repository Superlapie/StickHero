package com.stickhero.platform.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.stickhero.game.renderstate.FighterRenderModel

class MotionTrailRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, fighter: FighterRenderModel) {
        if (fighter.motionTrail.size < 2) return
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        for (i in 1 until fighter.motionTrail.size) {
            val from = fighter.motionTrail[i - 1]
            val to = fighter.motionTrail[i]
            val alpha = (to.alpha * 115f).toInt().coerceIn(0, 115)
            paint.color = Color.argb(alpha, 255, 245, 190)
            paint.strokeWidth = 15f * to.alpha.coerceAtLeast(0.15f)
            canvas.drawLine(from.position.x, from.position.y, to.position.x, to.position.y, paint)
        }
    }
}

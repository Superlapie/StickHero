package com.stickhero.platform.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.stickhero.game.animation.JointId
import com.stickhero.game.animation.Skeleton
import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.renderstate.DebugVisualConfig
import com.stickhero.game.renderstate.FighterRenderModel

class StickmanSkeletonRenderer(private val paint: Paint) {
    fun draw(canvas: Canvas, fighter: FighterRenderModel, worldHeight: Float) {
        val scale = worldHeight / 720f
        val origin = com.stickhero.game.physics.Vec2(fighter.x, fighter.groundY)
        val pose = fighter.pose.pose
        val dir = if (fighter.facing == FacingDirection.Right) 1f else -1f
        val color = if (fighter.isHurt) Color.rgb(255, 108, 92) else fighter.color

        paint.style = Paint.Style.FILL
        paint.color = Color.argb(70, 24, 32, 38)
        canvas.drawOval(RectF(fighter.x - 58f * scale, fighter.groundY - 7f * scale, fighter.x + 58f * scale, fighter.groundY + 10f * scale), paint)

        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 11f * scale
        paint.color = color
        Skeleton.bones.forEach { bone ->
            val from = pose.worldPosition(bone.from, origin, fighter.facing)
            val to = pose.worldPosition(bone.to, origin, fighter.facing)
            canvas.drawLine(from.x, from.y, to.x, to.y, paint)
        }

        val head = pose.worldPosition(JointId.Head, origin, fighter.facing)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f * scale
        canvas.drawCircle(head.x, head.y, 18f * scale, paint)

        paint.style = Paint.Style.FILL
        paint.color = Color.argb(95, 255, 255, 255)
        canvas.drawCircle(head.x + dir * 7f * scale, head.y - 3f * scale, 3.5f * scale, paint)

        if (DebugVisualConfig.enabled) {
            drawDebug(canvas, fighter, worldHeight)
        }
    }

    private fun drawDebug(canvas: Canvas, fighter: FighterRenderModel, worldHeight: Float) {
        val origin = com.stickhero.game.physics.Vec2(fighter.x, fighter.groundY)
        val pose = fighter.pose.pose
        if (DebugVisualConfig.showSkeletonJoints) {
            paint.style = Paint.Style.FILL
            paint.color = Color.argb(180, 48, 128, 255)
            JointId.values().forEach { joint ->
                val p = pose.worldPosition(joint, origin, fighter.facing)
                canvas.drawCircle(p.x, p.y, 3.5f, paint)
            }
        }
        if (DebugVisualConfig.showHurtboxes) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f
            paint.color = Color.argb(150, 64, 220, 120)
            canvas.drawRect(fighter.x - 22f, fighter.groundY - 110f, fighter.x + 22f, fighter.groundY, paint)
        }
        if (DebugVisualConfig.showHitboxes && fighter.isAttacking) {
            val dir = if (fighter.facing == FacingDirection.Right) 1f else -1f
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f
            paint.color = Color.argb(150, 255, 210, 64)
            val rect = RectF(fighter.x, fighter.groundY - 104f, fighter.x + dir * 82f, fighter.groundY - 48f)
            canvas.drawRect(normalized(rect), paint)
        }
        if (DebugVisualConfig.showAnimationNames) {
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 16f * (worldHeight / 720f)
            paint.color = Color.argb(220, 20, 28, 34)
            val label = if (DebugVisualConfig.showAttackPhase && fighter.pose.attackPhase != null) {
                "${fighter.pose.clipId}:${fighter.pose.attackPhase}"
            } else {
                fighter.pose.clipId
            }
            canvas.drawText(label, fighter.x, fighter.groundY - 142f, paint)
        }
    }

    private fun normalized(rect: RectF): RectF {
        return RectF(
            rect.left.coerceAtMost(rect.right),
            rect.top.coerceAtMost(rect.bottom),
            rect.left.coerceAtLeast(rect.right),
            rect.top.coerceAtLeast(rect.bottom)
        )
    }
}

package com.stickhero.platform.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.stickhero.game.core.GamePhase

class CanvasGameRenderer(
    context: Context,
    private val inputMapper: AndroidTouchInputMapper
) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val arenaRenderer = ArenaRenderer(paint)
    private val hudRenderer = HudRenderer(paint)
    private val spriteRenderer = SpriteSheetFighterRenderer(context, paint)
    private val motionTrailRenderer = MotionTrailRenderer(paint)
    private val impactEffectRenderer = ImpactEffectRenderer(context, paint)

    fun draw(canvas: Canvas, frame: SceneFrame) {
        val w = canvas.width.toFloat()
        val h = canvas.height.toFloat()
        inputMapper.updateControlBounds(canvas.width, canvas.height)
        paint.shader = LinearGradient(0f, 0f, 0f, h, Color.rgb(151, 189, 211), Color.rgb(237, 239, 226), Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, w, h, paint)
        paint.shader = null

        if (frame is SceneFrame.Menu) {
            drawMenu(canvas, w, h)
            return
        }

        val snapshot = (frame as SceneFrame.Game).snapshot
        canvas.save()
        canvas.scale(w / WORLD_WIDTH, h / WORLD_HEIGHT)
        canvas.save()
        canvas.translate(snapshot.camera.offsetX, snapshot.camera.offsetY)
        arenaRenderer.draw(canvas, WORLD_WIDTH, WORLD_HEIGHT)
        snapshot.fighters.forEach { motionTrailRenderer.draw(canvas, it) }
        snapshot.fighters.forEach { spriteRenderer.draw(canvas, it, WORLD_HEIGHT) }
        impactEffectRenderer.draw(canvas, snapshot.impacts)
        canvas.restore()
        hudRenderer.draw(canvas, snapshot, WORLD_WIDTH)
        canvas.restore()

        drawControls(canvas)
        if (snapshot.phase == GamePhase.Win || snapshot.phase == GamePhase.Lose) {
            drawOverlay(canvas, snapshot.phase, w, h)
        }
    }

    private fun drawMenu(canvas: Canvas, width: Float, height: Float) {
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(120, 22, 30, 36)
        canvas.drawRect(0f, 0f, width, height, paint)

        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.WHITE
        paint.textSize = height * 0.11f
        canvas.drawText("STICK HERO", width / 2f, height * 0.30f, paint)

        drawMenuButton(canvas, MenuLayout.startBounds(canvas.width, canvas.height), "START")
        drawMenuButton(canvas, MenuLayout.debugBounds(canvas.width, canvas.height), "DEBUG")
    }

    private fun drawMenuButton(canvas: Canvas, rect: RectF, label: String) {
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(190, 39, 55, 70)
        canvas.drawRoundRect(rect, 8f, 8f, paint)
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = rect.height() * 0.34f
        canvas.drawText(label, rect.centerX(), rect.centerY() - (paint.descent() + paint.ascent()) / 2f, paint)
    }

    private fun drawControls(canvas: Canvas) {
        drawJoystick(canvas)
        drawButton(canvas, inputMapper.specialAttackBounds, "SP")
        drawButton(canvas, inputMapper.attackBounds, "ATK")
    }

    private fun drawJoystick(canvas: Canvas) {
        val cx = inputMapper.joystickBaseX
        val cy = inputMapper.joystickBaseY
        val radius = inputMapper.joystickRadius
        if (radius <= 0f) return
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(88, 20, 28, 34)
        canvas.drawCircle(cx, cy, radius * 1.04f, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.argb(170, 255, 255, 255)
        canvas.drawCircle(cx, cy, radius, paint)
        paint.strokeWidth = 2f
        paint.color = Color.argb(95, 255, 255, 255)
        canvas.drawLine(cx - radius * 0.75f, cy, cx + radius * 0.75f, cy, paint)
        canvas.drawLine(cx, cy - radius * 0.75f, cx, cy + radius * 0.75f, paint)
        val knobX = cx + inputMapper.joystickNormalizedX * radius * 0.68f
        val knobY = cy + inputMapper.joystickNormalizedY * radius * 0.68f
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(205, 41, 54, 66)
        canvas.drawCircle(knobX, knobY, inputMapper.joystickKnobRadius, paint)
        paint.color = Color.argb(210, 255, 255, 255)
        canvas.drawCircle(knobX - radius * 0.12f, knobY - radius * 0.12f, inputMapper.joystickKnobRadius * 0.28f, paint)
    }

    private fun drawButton(canvas: Canvas, rect: RectF, label: String) {
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(165, 39, 55, 70)
        canvas.drawRoundRect(rect, 8f, 8f, paint)
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = rect.height() * 0.34f
        paint.style = Paint.Style.FILL
        canvas.drawText(label, rect.centerX(), rect.centerY() - (paint.descent() + paint.ascent()) / 2f, paint)
    }

    private fun drawOverlay(canvas: Canvas, phase: GamePhase, width: Float, height: Float) {
        paint.style = Paint.Style.FILL
        paint.color = Color.argb(185, 18, 24, 30)
        canvas.drawRect(0f, 0f, width, height, paint)
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = height * 0.11f
        canvas.drawText(if (phase == GamePhase.Win) "WIN" else "LOSE", width / 2f, height * 0.45f, paint)
        drawButton(canvas, inputMapper.restartBounds(width.toInt(), height.toInt()), "RESTART")
    }

    private companion object {
        const val WORLD_WIDTH = 1280f
        const val WORLD_HEIGHT = 720f
    }
}

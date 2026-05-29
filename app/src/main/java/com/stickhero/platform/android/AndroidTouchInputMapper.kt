package com.stickhero.platform.android

import android.graphics.RectF
import android.view.MotionEvent
import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class AndroidTouchInputMapper {
    private val activePointers = mutableMapOf<Int, Pair<Float, Float>>()
    private var restartPressed = false
    private var joystickPointerId: Int? = null
    private var joystickX = 0f
    private var joystickY = 0f

    @Synchronized
    fun onTouch(event: MotionEvent, width: Int, height: Int) {
        updateControlBounds(width, height)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val id = event.getPointerId(index)
                val x = event.getX(index)
                val y = event.getY(index)
                activePointers[id] = x to y
                if (joystickPointerId == null && joystickTouchBounds.contains(x, y)) {
                    joystickPointerId = id
                    updateJoystick(x, y)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until event.pointerCount) {
                    val id = event.getPointerId(index)
                    val x = event.getX(index)
                    val y = event.getY(index)
                    activePointers[id] = x to y
                    if (joystickPointerId == id) updateJoystick(x, y)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                val index = event.actionIndex
                val id = event.getPointerId(index)
                val x = event.getX(index)
                val y = event.getY(index)
                if (restartBounds(width, height).contains(x, y)) restartPressed = true
                activePointers.remove(id)
                if (joystickPointerId == id || event.actionMasked == MotionEvent.ACTION_CANCEL) {
                    joystickPointerId = null
                    joystickX = 0f
                    joystickY = 0f
                }
                if (event.actionMasked == MotionEvent.ACTION_CANCEL) activePointers.clear()
            }
        }
    }

    @Synchronized
    fun currentInput(): InputState {
        val commands = mutableSetOf<GameCommand>()
        if (joystickX < -0.28f) commands += GameCommand.MoveLeft
        if (joystickX > 0.28f) commands += GameCommand.MoveRight
        if (joystickY < -0.55f) commands += GameCommand.Jump
        if (joystickY > 0.55f) commands += GameCommand.Crouch
        for ((x, y) in activePointers.values) {
            if (attackBounds.contains(x, y)) commands += GameCommand.Attack
        }
        if (restartPressed) {
            commands += GameCommand.Restart
            restartPressed = false
        }
        return InputState(commands, joystickX, joystickY)
    }

    fun updateControlBounds(width: Int, height: Int) {
        val stickRadius = height * 0.13f
        val margin = height * 0.06f
        val bottom = height - margin
        joystickBaseX = margin + stickRadius
        joystickBaseY = bottom - stickRadius
        joystickRadius = stickRadius
        joystickKnobRadius = stickRadius * 0.42f
        joystickTouchBounds.set(
            joystickBaseX - stickRadius * 1.55f,
            joystickBaseY - stickRadius * 1.55f,
            joystickBaseX + stickRadius * 1.55f,
            joystickBaseY + stickRadius * 1.55f
        )
        val attack = height * 0.19f
        attackBounds.set(width - margin - attack, bottom - attack, width - margin, bottom)
    }

    fun restartBounds(width: Int, height: Int): RectF {
        val w = width * 0.28f
        val h = height * 0.12f
        return RectF(width / 2f - w / 2f, height * 0.58f, width / 2f + w / 2f, height * 0.58f + h)
    }

    val attackBounds = RectF()
    val joystickTouchBounds = RectF()
    var joystickBaseX = 0f
        private set
    var joystickBaseY = 0f
        private set
    var joystickRadius = 0f
        private set
    var joystickKnobRadius = 0f
        private set
    val joystickNormalizedX: Float
        get() = joystickX
    val joystickNormalizedY: Float
        get() = joystickY

    private fun updateJoystick(x: Float, y: Float) {
        val dx = x - joystickBaseX
        val dy = y - joystickBaseY
        val distance = hypot(dx, dy)
        if (distance <= joystickRadius || distance == 0f) {
            joystickX = (dx / joystickRadius).coerceIn(-1f, 1f)
            joystickY = (dy / joystickRadius).coerceIn(-1f, 1f)
        } else {
            val angle = atan2(dy, dx)
            joystickX = cos(angle)
            joystickY = sin(angle)
        }
    }
}

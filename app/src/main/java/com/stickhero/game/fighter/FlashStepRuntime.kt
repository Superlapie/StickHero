package com.stickhero.game.fighter

import com.stickhero.game.physics.Vec2

data class FlashStepRuntime(
    val destination: Vec2,
    val elapsedSeconds: Float = 0f,
    val teleported: Boolean = false
)

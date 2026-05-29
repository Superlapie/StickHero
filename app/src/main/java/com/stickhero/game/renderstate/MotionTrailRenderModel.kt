package com.stickhero.game.renderstate

import com.stickhero.game.physics.Vec2

data class MotionTrailPoint(
    val position: Vec2,
    val ageSeconds: Float,
    val durationSeconds: Float
) {
    val alpha: Float
        get() = (1f - ageSeconds / durationSeconds).coerceIn(0f, 1f)
}

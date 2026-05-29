package com.stickhero.game.renderstate

import com.stickhero.game.physics.Vec2

data class ImpactEffectRenderModel(
    val position: Vec2,
    val ageSeconds: Float = 0f,
    val durationSeconds: Float = 0.18f,
    val strength: Float = 1f
) {
    val progress: Float
        get() = (ageSeconds / durationSeconds).coerceIn(0f, 1f)
}

package com.stickhero.game.renderstate

import com.stickhero.game.animation.StickFrame

data class FighterPoseRenderModel(
    val frame: StickFrame,
    val clipId: String,
    val attackPhase: String?,
    val attackElapsedSeconds: Float = 0f,
    val attackVisualElapsedSeconds: Float = attackElapsedSeconds
)

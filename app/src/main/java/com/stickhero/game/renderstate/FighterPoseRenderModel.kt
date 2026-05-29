package com.stickhero.game.renderstate

import com.stickhero.game.animation.Pose

data class FighterPoseRenderModel(
    val pose: Pose,
    val clipId: String,
    val attackPhase: String?
)

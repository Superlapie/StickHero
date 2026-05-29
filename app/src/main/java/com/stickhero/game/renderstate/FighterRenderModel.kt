package com.stickhero.game.renderstate

import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.FighterId
import com.stickhero.game.fighter.FighterState

data class FighterRenderModel(
    val id: FighterId,
    val x: Float,
    val groundY: Float,
    val facing: FacingDirection,
    val state: FighterState,
    val healthFraction: Float,
    val color: Int,
    val animationTime: Float,
    val velocityX: Float,
    val velocityY: Float,
    val crouchAmount: Float,
    val isAttacking: Boolean,
    val isHurt: Boolean,
    val pose: FighterPoseRenderModel,
    val motionTrail: List<MotionTrailPoint>
)

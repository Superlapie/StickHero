package com.stickhero.game.fighter

import com.stickhero.game.combat.ActiveAttack
import com.stickhero.game.animation.PoseLibrary
import com.stickhero.game.animation.StickAnimationLibrary
import com.stickhero.game.animation.StickFrame
import com.stickhero.game.physics.Vec2
import com.stickhero.game.renderstate.MotionTrailPoint

data class FighterRuntimeState(
    var health: Int,
    var state: FighterState = FighterState.Idle,
    var facing: FacingDirection = FacingDirection.Right,
    var velocity: Vec2 = Vec2(),
    var knockbackVelocity: Vec2 = Vec2(),
    var isGrounded: Boolean = true,
    var crouchAmount: Float = 0f,
    var hitstunRemaining: Float = 0f,
    var activeAttack: ActiveAttack? = null,
    var animationTime: Float = 0f,
    var animationClipId: String = PoseLibrary.IDLE_BREATHE,
    var frame: StickFrame = StickAnimationLibrary.combatGuardFrame(),
    val motionTrail: MutableList<MotionTrailPoint> = mutableListOf()
)

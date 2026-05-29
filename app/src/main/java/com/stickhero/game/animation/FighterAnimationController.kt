package com.stickhero.game.animation

import com.stickhero.game.fighter.Fighter
import com.stickhero.game.fighter.FighterState
import com.stickhero.game.renderstate.MotionTrailPoint

class FighterAnimationController(
    private val clock: AnimationClock = AnimationClock(),
    private val player: AnimationPlayer = AnimationPlayer()
) {
    fun update(fighter: Fighter, moving: Boolean, crouching: Boolean, deltaSeconds: Float) {
        ageMotionTrail(fighter, deltaSeconds)
        if (fighter.runtime.activeAttack == null && fighter.runtime.hitstunRemaining <= 0f) {
            fighter.runtime.state = when {
                fighter.runtime.state == FighterState.Knockout -> FighterState.Knockout
                !fighter.runtime.isGrounded -> FighterState.Jump
                crouching -> FighterState.Crouch
                moving -> FighterState.Walk
                else -> FighterState.Idle
            }
        }
        val clipId = desiredClipId(fighter)
        if (clipId != fighter.runtime.animationClipId && fighter.runtime.activeAttack == null) {
            fighter.runtime.animationTime = 0f
        }
        fighter.runtime.animationClipId = clipId
        val clip = PoseLibrary.clip(clipId)
        fighter.runtime.animationTime = player.advanceTime(clock.advance(fighter.runtime.animationTime, 0f), clip, deltaSeconds)
        fighter.runtime.pose = when (fighter.runtime.state) {
            FighterState.Crouch -> PoseLibrary.crouchPose(fighter.runtime.crouchAmount)
            else -> player.sample(clip, fighter.runtime.animationTime)
        }
        updateMotionTrail(fighter)
    }

    private fun desiredClipId(fighter: Fighter): String {
        fighter.runtime.activeAttack?.let { return it.definition.animationClipId }
        return when (fighter.runtime.state) {
            FighterState.Walk -> PoseLibrary.WALK_FORWARD
            FighterState.Jump -> PoseLibrary.JUMP
            FighterState.Crouch -> PoseLibrary.CROUCH
            FighterState.Attack -> fighter.runtime.animationClipId
            FighterState.Hurt -> fighter.runtime.animationClipId
            FighterState.Knockout -> PoseLibrary.KNOCKOUT
            else -> PoseLibrary.IDLE_BREATHE
        }
    }

    private fun updateMotionTrail(fighter: Fighter) {
        val attack = fighter.runtime.activeAttack ?: return
        val clip = attack.definition.animationClipId
        val fastWindow = attack.elapsed >= attack.definition.startupDuration * 0.75f &&
            attack.elapsed <= attack.definition.startupDuration + attack.definition.activeDuration + 0.04f
        if (!fastWindow) return
        val hand = fighter.runtime.pose.worldPosition(JointId.RightHand, fighter.position, fighter.runtime.facing)
        val duration = if (clip == PoseLibrary.HEAVY_PUNCH) 0.18f else 0.12f
        fighter.runtime.motionTrail += MotionTrailPoint(hand, ageSeconds = 0f, durationSeconds = duration)
        if (fighter.runtime.motionTrail.size > 8) {
            fighter.runtime.motionTrail.removeAt(0)
        }
    }

    private fun ageMotionTrail(fighter: Fighter, deltaSeconds: Float) {
        val trail = fighter.runtime.motionTrail
        for (i in trail.indices) {
            val point = trail[i]
            trail[i] = point.copy(ageSeconds = point.ageSeconds + deltaSeconds)
        }
        trail.removeAll { it.ageSeconds >= it.durationSeconds }
    }
}

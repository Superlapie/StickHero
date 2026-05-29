package com.stickhero.game.physics

import com.stickhero.game.fighter.Fighter
import com.stickhero.game.fighter.FighterState
import com.stickhero.game.input.InputState
import kotlin.math.abs

class MovementSystem(private val bounds: Bounds) {
    fun update(fighter: Fighter, input: InputState, deltaSeconds: Float) {
        if (fighter.runtime.state == FighterState.Knockout) return
        val canMove = fighter.runtime.state != FighterState.Attack && fighter.runtime.state != FighterState.Hurt
        val crouching = fighter.runtime.isGrounded && input.wantsCrouch() && canMove
        fighter.runtime.crouchAmount = approach(fighter.runtime.crouchAmount, if (crouching) 1f else 0f, deltaSeconds * 9f)

        if (input.wantsJump() && fighter.runtime.isGrounded && !crouching && canMove) {
            fighter.runtime.velocity = fighter.runtime.velocity.copy(y = fighter.stats.jumpVelocity)
            fighter.runtime.isGrounded = false
            fighter.runtime.state = FighterState.Jump
        }

        val control = if (fighter.runtime.isGrounded) 1f else fighter.stats.airControl
        val targetX = if (canMove && !crouching) input.horizontalAxis() * fighter.stats.moveSpeed else 0f
        val currentX = fighter.runtime.velocity.x
        val nextX = if (abs(targetX) > 0f) {
            approach(currentX, targetX, fighter.stats.acceleration * control * deltaSeconds)
        } else {
            approach(currentX, 0f, fighter.stats.groundFriction * control * deltaSeconds)
        }

        val nextY = if (fighter.runtime.isGrounded) 0f else fighter.runtime.velocity.y + fighter.stats.gravity * deltaSeconds
        fighter.runtime.velocity = Vec2(nextX + fighter.runtime.knockbackVelocity.x, nextY)

        val nextPosition = fighter.position + fighter.runtime.velocity * deltaSeconds
        val groundedY = bounds.groundY
        fighter.position = fighter.position.copy(
            x = nextPosition.x.coerceIn(bounds.minX, bounds.maxX),
            y = if (nextPosition.y >= groundedY) groundedY else nextPosition.y
        )
        fighter.runtime.isGrounded = fighter.position.y >= groundedY
        if (fighter.runtime.isGrounded) {
            fighter.runtime.velocity = fighter.runtime.velocity.copy(y = 0f)
        }
        fighter.runtime.knockbackVelocity = fighter.runtime.knockbackVelocity * 0.84f
    }

    private fun approach(current: Float, target: Float, maxDelta: Float): Float {
        return when {
            current < target -> (current + maxDelta).coerceAtMost(target)
            current > target -> (current - maxDelta).coerceAtLeast(target)
            else -> target
        }
    }
}

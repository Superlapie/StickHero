package com.stickhero.game.core

import com.stickhero.game.fighter.Fighter
import com.stickhero.game.fighter.FighterState
import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import com.stickhero.game.match.MatchController
import com.stickhero.game.renderstate.CameraRenderState
import com.stickhero.game.renderstate.FighterRenderModel
import com.stickhero.game.renderstate.FighterPoseRenderModel
import com.stickhero.game.renderstate.HudRenderModel
import com.stickhero.game.renderstate.RenderSnapshot
import kotlin.math.sin

class StickHeroGame(private val config: GameConfig) {
    private var world = GameWorld.create(config)
    private var matchController = MatchController(config)

    fun update(deltaSeconds: Float, input: InputState): RenderSnapshot {
        if (input.has(GameCommand.Restart)) restart()
        matchController.update(world, input, deltaSeconds)
        return snapshot()
    }

    private fun restart() {
        world = GameWorld.create(config)
        matchController = MatchController(config)
    }

    private fun snapshot(): RenderSnapshot {
        return RenderSnapshot(
            phase = world.phase,
            fighters = listOf(world.player.toRenderModel(), world.enemy.toRenderModel()),
            hud = HudRenderModel(
                playerHealthFraction = world.player.runtime.health.toFloat() / world.player.stats.maxHealth,
                enemyHealthFraction = world.enemy.runtime.health.toFloat() / world.enemy.stats.maxHealth
            ),
            impacts = world.impactEffects.toList(),
            camera = cameraSnapshot()
        )
    }

    private fun Fighter.toRenderModel(): FighterRenderModel = FighterRenderModel(
        id = id,
        x = position.x,
        groundY = position.y,
        facing = runtime.facing,
        state = runtime.state,
        healthFraction = runtime.health.toFloat() / stats.maxHealth,
        color = color,
        animationTime = runtime.animationTime,
        velocityX = runtime.velocity.x,
        velocityY = runtime.velocity.y,
        crouchAmount = runtime.crouchAmount,
        isAttacking = runtime.activeAttack != null,
        isHurt = runtime.state == FighterState.Hurt,
        pose = FighterPoseRenderModel(
            pose = runtime.pose,
            clipId = runtime.animationClipId,
            attackPhase = runtime.activeAttack?.phaseName()
        ),
        motionTrail = runtime.motionTrail.toList()
    )

    private fun cameraSnapshot(): CameraRenderState {
        if (world.cameraShakeRemaining <= 0f) return CameraRenderState()
        val time = world.player.runtime.animationTime + world.enemy.runtime.animationTime
        val strength = world.cameraShakeStrength * (world.cameraShakeRemaining / 0.12f).coerceIn(0f, 1f)
        return CameraRenderState(
            offsetX = sin(time * 91f) * strength,
            offsetY = sin(time * 143f + 0.7f) * strength * 0.65f
        )
    }

    private fun com.stickhero.game.combat.ActiveAttack.phaseName(): String {
        return when {
            elapsed < definition.startupDuration -> "startup"
            elapsed < definition.startupDuration + definition.activeDuration -> "active"
            else -> "recovery"
        }
    }
}

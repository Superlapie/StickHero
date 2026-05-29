package com.stickhero.game.match

import com.stickhero.game.ai.BasicEnemyAI
import com.stickhero.game.animation.FighterAnimationController
import com.stickhero.game.combat.CombatSystem
import com.stickhero.game.core.GameConfig
import com.stickhero.game.core.GamePhase
import com.stickhero.game.core.GameWorld
import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.Fighter
import com.stickhero.game.input.InputState
import com.stickhero.game.physics.MovementSystem
import com.stickhero.game.physics.Vec2
import com.stickhero.game.renderstate.ImpactEffectRenderModel
import kotlin.math.abs
import kotlin.math.max

class MatchController(private val config: GameConfig) {
    private val movementSystem = MovementSystem(config.stageBounds)
    private val combatSystem = CombatSystem(config.defaultAttack)
    private val animationController = FighterAnimationController()
    private val winConditionSystem = WinConditionSystem()
    private val enemyAI = BasicEnemyAI(config.defaultAttack.range)

    fun update(world: GameWorld, playerInput: InputState, deltaSeconds: Float) {
        if (world.phase != GamePhase.Playing) return

        updateFacing(world.player, world.enemy)
        val enemyInput = enemyAI.decide(world.enemy, world.player).inputState
        updateEffects(world, deltaSeconds)

        if (world.hitstopRemaining > 0f) {
            world.hitstopRemaining = (world.hitstopRemaining - deltaSeconds).coerceAtLeast(0f)
            val slowedDelta = deltaSeconds * 0.08f
            animationController.update(world.player, abs(playerInput.horizontalAxis()) > 0.08f, playerInput.wantsCrouch(), slowedDelta)
            animationController.update(world.enemy, abs(enemyInput.horizontalAxis()) > 0.08f, enemyInput.wantsCrouch(), slowedDelta)
            return
        }

        movementSystem.update(world.player, playerInput, deltaSeconds)
        movementSystem.update(world.enemy, enemyInput, deltaSeconds)

        val playerHits = combatSystem.update(world.player, world.enemy, playerInput, deltaSeconds)
        val enemyHits = combatSystem.update(world.enemy, world.player, enemyInput, deltaSeconds)
        playerHits.forEach {
            combatSystem.applyDamage(world.player, world.enemy, it)
            registerImpact(world, world.player, world.enemy, it.hitstopDuration, it.impactShake)
        }
        enemyHits.forEach {
            combatSystem.applyDamage(world.enemy, world.player, it)
            registerImpact(world, world.enemy, world.player, it.hitstopDuration, it.impactShake)
        }

        animationController.update(world.player, abs(playerInput.horizontalAxis()) > 0.08f, playerInput.wantsCrouch(), deltaSeconds)
        animationController.update(world.enemy, abs(enemyInput.horizontalAxis()) > 0.08f, enemyInput.wantsCrouch(), deltaSeconds)
        winConditionSystem.update(world)
    }

    private fun registerImpact(world: GameWorld, attacker: Fighter, target: Fighter, hitstopDuration: Float, shake: Float) {
        val direction = if (target.position.x >= attacker.position.x) 1f else -1f
        world.hitstopRemaining = max(world.hitstopRemaining, hitstopDuration)
        world.cameraShakeRemaining = max(world.cameraShakeRemaining, 0.12f * shake.coerceAtLeast(0.25f))
        world.cameraShakeStrength = max(world.cameraShakeStrength, 8f * shake)
        world.impactEffects += ImpactEffectRenderModel(
            position = Vec2(target.position.x - direction * 24f, target.position.y - 88f),
            strength = shake.coerceIn(0.35f, 1.2f)
        )
    }

    private fun updateEffects(world: GameWorld, deltaSeconds: Float) {
        for (i in world.impactEffects.indices) {
            val effect = world.impactEffects[i]
            world.impactEffects[i] = effect.copy(ageSeconds = effect.ageSeconds + deltaSeconds)
        }
        world.impactEffects.removeAll { it.ageSeconds >= it.durationSeconds }
        world.cameraShakeRemaining = (world.cameraShakeRemaining - deltaSeconds).coerceAtLeast(0f)
        if (world.cameraShakeRemaining == 0f) {
            world.cameraShakeStrength = 0f
        }
    }

    private fun updateFacing(left: Fighter, right: Fighter) {
        left.runtime.facing = if (right.position.x >= left.position.x) FacingDirection.Right else FacingDirection.Left
        right.runtime.facing = if (left.position.x >= right.position.x) FacingDirection.Right else FacingDirection.Left
    }
}

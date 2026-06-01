package com.stickhero.game.match

import com.stickhero.game.ai.BasicEnemyAI
import com.stickhero.game.animation.FighterAnimationController
import com.stickhero.game.combat.CombatSystem
import com.stickhero.game.core.GameConfig
import com.stickhero.game.core.GameMode
import com.stickhero.game.core.GamePhase
import com.stickhero.game.core.GameWorld
import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.FighterState
import com.stickhero.game.fighter.FlashStepRuntime
import com.stickhero.game.fighter.Fighter
import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import com.stickhero.game.physics.MovementSystem
import com.stickhero.game.physics.Vec2
import kotlin.math.abs
import kotlin.math.max

class MatchController(private val config: GameConfig) {
    private val movementSystem = MovementSystem(config.stageBounds)
    private val combatSystem = CombatSystem(config.defaultAttack, config.specialAttacks)
    private val animationController = FighterAnimationController()
    private val winConditionSystem = WinConditionSystem()
    private val enemyAI = BasicEnemyAI(config.defaultAttack.range)
    private var playerFlashStepWasPressed = false

    fun update(world: GameWorld, playerInput: InputState, deltaSeconds: Float) {
        if (world.phase != GamePhase.Playing) return
        updateEffects(world, deltaSeconds)
        val flashStepPressed = playerInput.has(GameCommand.FlashStep)

        val enemy = world.enemy
        if (world.mode == GameMode.DebugSandbox || enemy == null) {
            if (world.hitstopRemaining > 0f) {
                world.hitstopRemaining = (world.hitstopRemaining - deltaSeconds).coerceAtLeast(0f)
                val slowedDelta = deltaSeconds * 0.08f
                animationController.update(world.player, abs(playerInput.horizontalAxis()) > 0.08f, playerInput.wantsCrouch(), slowedDelta)
                playerFlashStepWasPressed = flashStepPressed
                return
            }

            updatePlayerFacing(world.player, playerInput)
            val playerIsFlashing = updateFlashStep(world.player, playerInput, !playerFlashStepWasPressed && flashStepPressed, deltaSeconds)
            if (!playerIsFlashing) {
                movementSystem.update(world.player, playerInput, deltaSeconds)
                combatSystem.update(world.player, null, playerInput, deltaSeconds)
            }
            animationController.update(world.player, abs(playerInput.horizontalAxis()) > 0.08f, playerInput.wantsCrouch(), deltaSeconds)
            winConditionSystem.update(world)
            playerFlashStepWasPressed = flashStepPressed
            return
        }

        updateFacing(world.player, playerInput, enemy)
        val enemyInput = enemyAI.decide(enemy, world.player).inputState

        if (world.hitstopRemaining > 0f) {
            world.hitstopRemaining = (world.hitstopRemaining - deltaSeconds).coerceAtLeast(0f)
            val slowedDelta = deltaSeconds * 0.08f
            animationController.update(world.player, abs(playerInput.horizontalAxis()) > 0.08f, playerInput.wantsCrouch(), slowedDelta)
            animationController.update(enemy, abs(enemyInput.horizontalAxis()) > 0.08f, enemyInput.wantsCrouch(), slowedDelta)
            playerFlashStepWasPressed = flashStepPressed
            return
        }

        val playerIsFlashing = updateFlashStep(world.player, playerInput, !playerFlashStepWasPressed && flashStepPressed, deltaSeconds)
        if (!playerIsFlashing) {
            movementSystem.update(world.player, playerInput, deltaSeconds)
        }
        movementSystem.update(enemy, enemyInput, deltaSeconds)

        val playerHits = if (playerIsFlashing) emptyList() else combatSystem.update(world.player, enemy, playerInput, deltaSeconds)
        val enemyHits = combatSystem.update(enemy, world.player, enemyInput, deltaSeconds)
        playerHits.forEach {
            combatSystem.applyDamage(world.player, enemy, it)
            if (it.showImpactEffect) registerImpact(world, world.player, enemy, it.hitstopDuration, it.impactShake)
        }
        enemyHits.forEach {
            combatSystem.applyDamage(enemy, world.player, it)
            if (it.showImpactEffect) registerImpact(world, enemy, world.player, it.hitstopDuration, it.impactShake)
        }

        animationController.update(world.player, abs(playerInput.horizontalAxis()) > 0.08f, playerInput.wantsCrouch(), deltaSeconds)
        animationController.update(enemy, abs(enemyInput.horizontalAxis()) > 0.08f, enemyInput.wantsCrouch(), deltaSeconds)
        winConditionSystem.update(world)
        playerFlashStepWasPressed = flashStepPressed
    }

    private fun registerImpact(world: GameWorld, attacker: Fighter, target: Fighter, hitstopDuration: Float, shake: Float) {
        world.hitstopRemaining = max(world.hitstopRemaining, hitstopDuration)
        world.cameraShakeRemaining = max(world.cameraShakeRemaining, 0.12f * shake.coerceAtLeast(0.25f))
        world.cameraShakeStrength = max(world.cameraShakeStrength, 8f * shake)
    }

    private fun updateEffects(world: GameWorld, deltaSeconds: Float) {
        world.cameraShakeRemaining = (world.cameraShakeRemaining - deltaSeconds).coerceAtLeast(0f)
        if (world.cameraShakeRemaining == 0f) {
            world.cameraShakeStrength = 0f
        }
    }

    private fun updateFlashStep(fighter: Fighter, input: InputState, shouldStart: Boolean, deltaSeconds: Float): Boolean {
        val active = fighter.runtime.flashStep
        if (active != null) {
            val elapsed = active.elapsedSeconds + deltaSeconds
            val shouldTeleport = !active.teleported && elapsed >= FlashStepTeleportSeconds
            if (shouldTeleport) {
                fighter.position = active.destination
                fighter.runtime.velocity = Vec2()
            }
            if (elapsed >= FlashStepDurationSeconds) {
                fighter.runtime.flashStep = null
                if (fighter.runtime.state == FighterState.FlashStep) fighter.runtime.state = FighterState.Idle
                return false
            }
            fighter.runtime.flashStep = active.copy(elapsedSeconds = elapsed, teleported = active.teleported || shouldTeleport)
            fighter.runtime.state = FighterState.FlashStep
            return true
        }

        if (!shouldStart || fighter.runtime.activeAttack != null || fighter.runtime.hitstunRemaining > 0f || fighter.runtime.state == FighterState.Knockout) {
            return false
        }

        val direction = flashStepDirection(fighter, input)
        fighter.runtime.facing = if (direction > 0f) FacingDirection.Right else FacingDirection.Left
        fighter.runtime.flashStep = FlashStepRuntime(
            destination = fighter.position.copy(
                x = (fighter.position.x + FlashStepDistance * direction).coerceIn(config.stageBounds.minX, config.stageBounds.maxX)
            )
        )
        fighter.runtime.state = FighterState.FlashStep
        fighter.runtime.animationTime = 0f
        fighter.runtime.velocity = Vec2()
        return true
    }

    private fun flashStepDirection(fighter: Fighter, input: InputState): Float {
        val horizontal = input.horizontalAxis()
        if (abs(horizontal) > 0.08f) return if (horizontal > 0f) 1f else -1f
        return if (fighter.runtime.facing == FacingDirection.Right) 1f else -1f
    }

    private fun updateFacing(left: Fighter, leftInput: InputState, right: Fighter?) {
        if (right == null) return
        updatePlayerFacing(left, leftInput, right)
        right.runtime.facing = if (left.position.x >= right.position.x) FacingDirection.Right else FacingDirection.Left
    }

    private fun updatePlayerFacing(player: Fighter, input: InputState, fallbackTarget: Fighter? = null) {
        val horizontal = input.horizontalAxis()
        player.runtime.facing = when {
            horizontal > 0.08f -> FacingDirection.Right
            horizontal < -0.08f -> FacingDirection.Left
            fallbackTarget != null && fallbackTarget.position.x >= player.position.x -> FacingDirection.Right
            fallbackTarget != null -> FacingDirection.Left
            else -> player.runtime.facing
        }
    }

    private companion object {
        const val FlashStepDurationSeconds = 0.52f
        const val FlashStepTeleportSeconds = FlashStepDurationSeconds * 0.5f
        const val FlashStepDistance = 235f
    }
}

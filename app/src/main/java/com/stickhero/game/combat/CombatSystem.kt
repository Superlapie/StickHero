package com.stickhero.game.combat

import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.Fighter
import com.stickhero.game.fighter.FighterState
import com.stickhero.game.animation.PoseLibrary
import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import com.stickhero.game.physics.Vec2

class CombatSystem(
    private val defaultAttack: AttackDefinition,
    private val specialAttacks: Map<GameCommand, AttackDefinition>
) {
    fun update(attacker: Fighter, target: Fighter?, commands: InputState, deltaSeconds: Float): List<DamageEvent> {
        if (!attacker.isAlive()) return emptyList()
        updateHitstun(attacker, deltaSeconds)
        maybeStartAttack(attacker, commands)
        val attack = attacker.runtime.activeAttack ?: return emptyList()
        attack.elapsed += deltaSeconds
        attack.visualElapsed += deltaSeconds
        holdActivePhase(attack, commands)

        val events = mutableListOf<DamageEvent>()
        if (target != null && attack.isActive && target.isAlive() && target.id !in attack.hitTargets && inRange(attacker, target, attack.definition.range)) {
            attack.hitTargets += target.id
            events += DamageEvent(
                attacker.id,
                target.id,
                attack.definition.damage,
                attack.definition.knockback,
                attack.definition.hitstunDuration,
                attack.definition.hitstopDuration,
                attack.definition.impactShake
            )
        }
        if (attack.isFinished) {
            attacker.runtime.activeAttack = null
            if (attacker.runtime.state != FighterState.Knockout) attacker.runtime.state = FighterState.Idle
        }
        return events
    }

    fun applyDamage(attacker: Fighter, target: Fighter, event: DamageEvent) {
        if (!target.isAlive()) return
        val direction = if (target.position.x >= attacker.position.x) 1f else -1f
        target.runtime.health = (target.runtime.health - event.damage).coerceAtLeast(0)
        target.runtime.activeAttack = null
        target.runtime.hitstunRemaining = event.hitstunDuration
        target.runtime.knockbackVelocity = Vec2(event.knockback.horizontal * direction, event.knockback.vertical)
        target.runtime.state = if (target.runtime.health == 0) FighterState.Knockout else FighterState.Hurt
        target.runtime.animationClipId = when {
            target.runtime.health == 0 -> PoseLibrary.KNOCKOUT
            event.damage >= 15 || event.impactShake >= 0.9f -> PoseLibrary.HURT_HEAVY
            else -> PoseLibrary.HURT_LIGHT
        }
        target.runtime.animationTime = 0f
    }

    private fun maybeStartAttack(fighter: Fighter, commands: InputState) {
        val definition = when {
            specialAttacks.any { (command, _) -> commands.has(command) } ->
                specialAttacks.entries.first { (command, _) -> commands.has(command) }.value
            commands.has(GameCommand.Attack) -> defaultAttack
            else -> return
        }
        if (fighter.runtime.activeAttack != null) return
        if (fighter.runtime.hitstunRemaining > 0f || fighter.runtime.state == FighterState.Knockout) return
        fighter.runtime.activeAttack = ActiveAttack(definition)
        fighter.runtime.state = FighterState.Attack
        fighter.runtime.animationTime = 0f
        fighter.runtime.animationClipId = definition.animationClipId
    }

    private fun holdActivePhase(attack: ActiveAttack, commands: InputState) {
        val holdCommand = attack.definition.holdActiveCommand ?: return
        if (!commands.has(holdCommand)) return
        val activeStart = attack.definition.startupDuration
        val activeEnd = activeStart + attack.definition.activeDuration
        if (attack.elapsed in activeStart..activeEnd) {
            attack.elapsed = activeStart + attack.definition.activeDuration * 0.5f
        }
    }

    private fun updateHitstun(fighter: Fighter, deltaSeconds: Float) {
        if (fighter.runtime.hitstunRemaining <= 0f) return
        fighter.runtime.hitstunRemaining = (fighter.runtime.hitstunRemaining - deltaSeconds).coerceAtLeast(0f)
        if (fighter.runtime.hitstunRemaining == 0f && fighter.runtime.state == FighterState.Hurt) {
            fighter.runtime.state = FighterState.Idle
        }
    }

    private fun inRange(attacker: Fighter, target: Fighter, range: Float): Boolean {
        val facingMultiplier = if (attacker.runtime.facing == FacingDirection.Right) 1f else -1f
        val forwardDistance = (target.position.x - attacker.position.x) * facingMultiplier
        return forwardDistance >= 0f && forwardDistance <= range + target.stats.hurtboxWidth / 2f
    }
}

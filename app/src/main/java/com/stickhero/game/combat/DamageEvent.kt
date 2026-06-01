package com.stickhero.game.combat

import com.stickhero.game.fighter.FighterId

data class DamageEvent(
    val attackerId: FighterId,
    val targetId: FighterId,
    val damage: Int,
    val knockback: Knockback,
    val hitstunDuration: Float,
    val hitstopDuration: Float,
    val impactShake: Float,
    val showImpactEffect: Boolean
)

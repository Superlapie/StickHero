package com.stickhero.game.combat

import com.stickhero.game.fighter.FighterId

data class ActiveAttack(
    val definition: AttackDefinition,
    var elapsed: Float = 0f,
    val hitTargets: MutableSet<FighterId> = mutableSetOf()
) {
    val isActive: Boolean
        get() = elapsed >= definition.startupDuration &&
            elapsed < definition.startupDuration + definition.activeDuration

    val isFinished: Boolean
        get() = elapsed >= definition.totalDuration
}

package com.stickhero.game.match

import com.stickhero.game.core.GameWorld

class WinConditionSystem(private val rules: MatchRules = MatchRules()) {
    fun update(world: GameWorld) {
        world.phase = rules.phaseFor(world)
    }
}

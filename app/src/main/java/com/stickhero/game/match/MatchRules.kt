package com.stickhero.game.match

import com.stickhero.game.core.GamePhase
import com.stickhero.game.core.GameWorld

class MatchRules {
    fun phaseFor(world: GameWorld): GamePhase = when {
        !world.player.isAlive() -> GamePhase.Lose
        !world.enemy.isAlive() -> GamePhase.Win
        else -> GamePhase.Playing
    }
}

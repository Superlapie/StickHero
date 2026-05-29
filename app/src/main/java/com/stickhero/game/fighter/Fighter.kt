package com.stickhero.game.fighter

import com.stickhero.game.physics.Vec2

data class Fighter(
    val id: FighterId,
    val stats: FighterStats,
    var position: Vec2,
    val color: Int,
    val runtime: FighterRuntimeState = FighterRuntimeState(stats.maxHealth)
) {
    fun isAlive(): Boolean = runtime.health > 0
}

package com.stickhero.game.core

import com.stickhero.game.combat.AttackDefinition
import com.stickhero.game.config.AttackCatalog
import com.stickhero.game.config.StageCatalog
import com.stickhero.game.physics.Bounds

data class GameConfig(
    val worldWidth: Float,
    val worldHeight: Float,
    val stageBounds: Bounds,
    val defaultAttack: AttackDefinition
) {
    companion object {
        fun default() = GameConfig(
            worldWidth = 1280f,
            worldHeight = 720f,
            stageBounds = StageCatalog.prototypeStage,
            defaultAttack = AttackCatalog.basicPunch
        )
    }
}

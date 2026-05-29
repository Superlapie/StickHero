package com.stickhero.game.core

import com.stickhero.game.config.FighterCatalog
import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.Fighter
import com.stickhero.game.renderstate.ImpactEffectRenderModel

data class GameWorld(
    val player: Fighter,
    val enemy: Fighter,
    var phase: GamePhase = GamePhase.Playing,
    var hitstopRemaining: Float = 0f,
    var cameraShakeRemaining: Float = 0f,
    var cameraShakeStrength: Float = 0f,
    val impactEffects: MutableList<ImpactEffectRenderModel> = mutableListOf()
) {
    companion object {
        fun create(config: GameConfig): GameWorld {
            val player = FighterCatalog.player(260f, config.stageBounds.groundY)
            val enemy = FighterCatalog.enemy(980f, config.stageBounds.groundY)
            player.runtime.facing = FacingDirection.Right
            enemy.runtime.facing = FacingDirection.Left
            return GameWorld(player, enemy)
        }
    }
}

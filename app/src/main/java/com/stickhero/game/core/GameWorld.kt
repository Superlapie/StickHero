package com.stickhero.game.core

import com.stickhero.game.config.FighterCatalog
import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.Fighter
import com.stickhero.game.renderstate.ImpactEffectRenderModel

data class GameWorld(
    val mode: GameMode,
    val player: Fighter,
    val enemy: Fighter?,
    var phase: GamePhase = GamePhase.Playing,
    var hitstopRemaining: Float = 0f,
    var cameraShakeRemaining: Float = 0f,
    var cameraShakeStrength: Float = 0f,
    val impactEffects: MutableList<ImpactEffectRenderModel> = mutableListOf()
) {
    companion object {
        fun create(config: GameConfig): GameWorld {
            val player = FighterCatalog.player(260f, config.stageBounds.groundY)
            val enemy = if (config.mode == GameMode.Normal) {
                FighterCatalog.enemy(980f, config.stageBounds.groundY).also {
                    it.runtime.facing = FacingDirection.Left
                }
            } else {
                null
            }
            player.runtime.facing = FacingDirection.Right
            return GameWorld(config.mode, player, enemy)
        }
    }
}

package com.stickhero.game.renderstate

import com.stickhero.game.core.GameMode

data class HudRenderModel(
    val playerHealthFraction: Float,
    val enemyHealthFraction: Float? = null,
    val mode: GameMode = GameMode.Normal
)

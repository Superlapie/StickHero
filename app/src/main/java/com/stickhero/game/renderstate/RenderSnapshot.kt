package com.stickhero.game.renderstate

import com.stickhero.game.core.GameMode
import com.stickhero.game.core.GamePhase

data class RenderSnapshot(
    val phase: GamePhase,
    val fighters: List<FighterRenderModel>,
    val hud: HudRenderModel,
    val impacts: List<ImpactEffectRenderModel> = emptyList(),
    val camera: CameraRenderState = CameraRenderState(),
    val mode: GameMode = GameMode.Normal
)

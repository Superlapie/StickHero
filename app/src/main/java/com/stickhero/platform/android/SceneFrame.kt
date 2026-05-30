package com.stickhero.platform.android

import com.stickhero.game.renderstate.RenderSnapshot

sealed interface SceneFrame {
    object Menu : SceneFrame
    data class Game(val snapshot: RenderSnapshot) : SceneFrame
}

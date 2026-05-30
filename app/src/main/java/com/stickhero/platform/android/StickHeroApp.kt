package com.stickhero.platform.android

import com.stickhero.game.core.GameConfig
import com.stickhero.game.core.GameMode
import com.stickhero.game.core.StickHeroGame
import com.stickhero.game.input.InputState

class StickHeroApp {
    private sealed interface Scene {
        object Menu : Scene
        data class Playing(val game: StickHeroGame) : Scene
    }

    private var scene: Scene = Scene.Menu

    fun isMenu(): Boolean = scene is Scene.Menu

    fun start(mode: GameMode) {
        scene = Scene.Playing(StickHeroGame(GameConfig.default(mode)))
    }

    fun update(deltaSeconds: Float, input: InputState): SceneFrame {
        return when (val current = scene) {
            Scene.Menu -> SceneFrame.Menu
            is Scene.Playing -> SceneFrame.Game(current.game.update(deltaSeconds, input))
        }
    }
}

package com.stickhero.game.physics

import com.stickhero.game.config.FighterCatalog
import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import kotlin.test.Test
import kotlin.test.assertEquals

class MovementSystemTest {
    @Test
    fun movementUsesDeltaTimeAndBounds() {
        val bounds = Bounds(minX = 0f, maxX = 120f, groundY = 520f)
        val system = MovementSystem(bounds)
        val fighter = FighterCatalog.player(100f, 520f)

        system.update(fighter, InputState(setOf(GameCommand.MoveRight)), 1f)

        assertEquals(120f, fighter.position.x)
        assertEquals(520f, fighter.position.y)
    }
}

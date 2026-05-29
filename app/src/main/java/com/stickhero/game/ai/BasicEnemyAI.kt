package com.stickhero.game.ai

import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import kotlin.math.abs

class BasicEnemyAI(private val attackRange: Float) : FighterAI {
    override fun decide(self: com.stickhero.game.fighter.Fighter, opponent: com.stickhero.game.fighter.Fighter): AICommand {
        if (!self.isAlive()) return AICommand(InputState())
        val distance = opponent.position.x - self.position.x
        val commands = mutableSetOf<GameCommand>()
        when {
            abs(distance) <= attackRange * 0.88f -> commands += GameCommand.Attack
            distance < 0f -> commands += GameCommand.MoveLeft
            distance > 0f -> commands += GameCommand.MoveRight
        }
        return AICommand(InputState(commands))
    }
}

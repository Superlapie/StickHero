package com.stickhero.game.input

import kotlin.math.abs

data class InputState(
    val commands: Set<GameCommand> = emptySet(),
    val moveX: Float = 0f,
    val moveY: Float = 0f
) {
    fun has(command: GameCommand) = command in commands

    fun horizontalAxis(): Float = when {
        abs(moveX) > 0.08f -> moveX.coerceIn(-1f, 1f)
        has(GameCommand.MoveLeft) && !has(GameCommand.MoveRight) -> -1f
        has(GameCommand.MoveRight) && !has(GameCommand.MoveLeft) -> 1f
        else -> 0f
    }

    fun wantsJump(): Boolean = has(GameCommand.Jump) || moveY < -0.55f

    fun wantsCrouch(): Boolean = has(GameCommand.Crouch) || moveY > 0.55f
}

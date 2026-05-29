package com.stickhero.game.physics

data class Vec2(val x: Float = 0f, val y: Float = 0f) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun times(scale: Float) = Vec2(x * scale, y * scale)
}

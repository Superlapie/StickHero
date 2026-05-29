package com.stickhero.game.fighter

data class FighterStats(
    val maxHealth: Int,
    val moveSpeed: Float,
    val acceleration: Float,
    val groundFriction: Float,
    val airControl: Float,
    val jumpVelocity: Float,
    val gravity: Float,
    val hurtboxWidth: Float,
    val hurtboxHeight: Float
)

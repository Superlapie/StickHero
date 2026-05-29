package com.stickhero.game.config

import com.stickhero.game.fighter.Fighter
import com.stickhero.game.fighter.FighterId
import com.stickhero.game.fighter.FighterStats
import com.stickhero.game.physics.Vec2

object FighterCatalog {
    private val defaultStats = FighterStats(
        maxHealth = 100,
        moveSpeed = 230f,
        acceleration = 1450f,
        groundFriction = 1850f,
        airControl = 0.45f,
        jumpVelocity = -640f,
        gravity = 1850f,
        hurtboxWidth = 44f,
        hurtboxHeight = 110f
    )

    fun player(startX: Float, groundY: Float) = Fighter(
        id = FighterId.Player,
        stats = defaultStats,
        position = Vec2(startX, groundY),
        color = 0xFF1F2937.toInt()
    )

    fun enemy(startX: Float, groundY: Float) = Fighter(
        id = FighterId.Enemy,
        stats = defaultStats.copy(moveSpeed = 180f),
        position = Vec2(startX, groundY),
        color = 0xFFB8323A.toInt()
    )
}

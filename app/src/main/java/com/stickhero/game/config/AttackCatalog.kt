package com.stickhero.game.config

import com.stickhero.game.combat.AttackDefinition
import com.stickhero.game.combat.Knockback
import com.stickhero.game.animation.PoseLibrary

object AttackCatalog {
    val basicPunch = AttackDefinition(
        id = "basic_punch",
        animationClipId = PoseLibrary.JAB,
        damage = 10,
        range = 72f,
        startupDuration = 0.12f,
        activeDuration = 0.09f,
        recoveryDuration = 0.28f,
        knockback = Knockback(horizontal = 150f),
        hitstunDuration = 0.22f,
        hitstopDuration = 0.042f,
        impactShake = 0.45f
    )

    val heavyPunch = AttackDefinition(
        id = "heavy_punch",
        animationClipId = PoseLibrary.HEAVY_PUNCH,
        damage = 18,
        range = 88f,
        startupDuration = 0.22f,
        activeDuration = 0.11f,
        recoveryDuration = 0.39f,
        knockback = Knockback(horizontal = 230f, vertical = -90f),
        hitstunDuration = 0.34f,
        hitstopDuration = 0.07f,
        impactShake = 1f
    )
}

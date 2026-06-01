package com.stickhero.game.config

import com.stickhero.game.combat.AttackDefinition
import com.stickhero.game.combat.Knockback
import com.stickhero.game.animation.PoseLibrary
import com.stickhero.game.input.GameCommand

object AttackCatalog {
    val basicPunch = AttackDefinition(
        id = "basic_punch",
        animationClipId = PoseLibrary.JAB,
        damage = 10,
        range = 72f,
        startupDuration = 0.16f,
        activeDuration = 0.12f,
        recoveryDuration = 0.44f,
        knockback = Knockback(horizontal = 150f),
        hitstunDuration = 0.22f,
        hitstopDuration = 0.042f,
        impactShake = 0.45f,
        showImpactEffect = true
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
        impactShake = 1f,
        showImpactEffect = false
    )

    val kamehameha = AttackDefinition(
        id = "kamehameha",
        animationClipId = PoseLibrary.HEAVY_PUNCH,
        damage = 24,
        range = 760f,
        startupDuration = 0.62f,
        activeDuration = 0.42f,
        recoveryDuration = 0.56f,
        knockback = Knockback(horizontal = 320f, vertical = -120f),
        hitstunDuration = 0.42f,
        hitstopDuration = 0.04f,
        impactShake = 1.2f,
        showImpactEffect = false,
        holdActiveCommand = GameCommand.SpecialAttack
    )

    val earthSmash = AttackDefinition(
        id = "earth_smash",
        animationClipId = PoseLibrary.EARTH_SMASH,
        damage = 22,
        range = 116f,
        startupDuration = 0.34f,
        activeDuration = 0.16f,
        recoveryDuration = 0.42f,
        knockback = Knockback(horizontal = 260f, vertical = -150f),
        hitstunDuration = 0.38f,
        hitstopDuration = 0.07f,
        impactShake = 1.1f,
        showImpactEffect = false
    )

    val bladeFlurry = AttackDefinition(
        id = "blade_flurry",
        animationClipId = PoseLibrary.BLADE_FLURRY,
        damage = 18,
        range = 132f,
        startupDuration = 0.36f,
        activeDuration = 0.72f,
        recoveryDuration = 0.72f,
        knockback = Knockback(horizontal = 210f, vertical = -60f),
        hitstunDuration = 0.34f,
        hitstopDuration = 0.045f,
        impactShake = 0.85f,
        showImpactEffect = false
    )

    val electroPulse = AttackDefinition(
        id = "electro_pulse",
        animationClipId = PoseLibrary.ELECTRO_PULSE,
        damage = 20,
        range = 148f,
        startupDuration = 0.36f,
        activeDuration = 0.34f,
        recoveryDuration = 0.58f,
        knockback = Knockback(horizontal = 180f, vertical = -110f),
        hitstunDuration = 0.40f,
        hitstopDuration = 0.055f,
        impactShake = 0.95f,
        showImpactEffect = false
    )

    val flameBurst = AttackDefinition(
        id = "flame_burst",
        animationClipId = PoseLibrary.FLAME_BURST,
        damage = 23,
        range = 158f,
        startupDuration = 0.38f,
        activeDuration = 0.38f,
        recoveryDuration = 0.69f,
        knockback = Knockback(horizontal = 240f, vertical = -130f),
        hitstunDuration = 0.42f,
        hitstopDuration = 0.06f,
        impactShake = 1.05f,
        showImpactEffect = false
    )

    val playerSpecials = linkedMapOf(
        GameCommand.SpecialAttack to kamehameha,
        GameCommand.EarthSmash to earthSmash,
        GameCommand.BladeFlurry to bladeFlurry,
        GameCommand.ElectroPulse to electroPulse,
        GameCommand.FlameBurst to flameBurst
    )
}

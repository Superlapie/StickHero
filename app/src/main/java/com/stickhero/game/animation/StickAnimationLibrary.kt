package com.stickhero.game.animation

import com.stickhero.game.physics.Vec2

object StickAnimationLibrary {
    const val COMBAT_IDLE = "combat_idle"
    const val SHUFFLE_FORWARD = "shuffle_forward"
    const val SHUFFLE_BACKWARD = "shuffle_backward"
    const val JAB = "jab"
    const val CROSS = "cross"
    const val HEAVY_PUNCH = "heavy_punch"
    const val EARTH_SMASH = "earth_smash"
    const val BLADE_FLURRY = "blade_flurry"
    const val ELECTRO_PULSE = "electro_pulse"
    const val FLAME_BURST = "flame_burst"
    const val HURT_LIGHT = "hurt_light"
    const val HURT_HEAVY = "hurt_heavy"
    const val KNOCKOUT = "knockout"
    const val JUMP = "jump"
    const val CROUCH = "crouch"

    private val clips = listOf(
        combatIdle(),
        shuffleForward(),
        shuffleBackward(),
        jump(),
        crouch(),
        jab(),
        cross(),
        heavyPunch(),
        specialClip(EARTH_SMASH, 0.92f),
        specialClip(BLADE_FLURRY, 1.80f),
        specialClip(ELECTRO_PULSE, 1.28f),
        specialClip(FLAME_BURST, 1.45f),
        hurtLight(),
        hurtHeavy(),
        knockout()
    ).associateBy { it.id }

    fun clip(id: String): StickAnimationClip = clips[id] ?: clips.getValue(COMBAT_IDLE)

    fun clips(): List<StickAnimationClip> = clips.values.toList()

    fun combatGuardFrame(): StickFrame = frame(
        head = p(0f, -156f),
        radius = 15f,
        neck = p(-2f, -140f),
        chest = p(10f, -116f),
        hips = p(-6f, -78f),
        leadShoulder = p(6f, -130f),
        leadElbow = p(36f, -116f),
        leadHand = p(50f, -100f),
        rearShoulder = p(-6f, -128f),
        rearElbow = p(-24f, -112f),
        rearHand = p(-8f, -98f),
        leadKnee = p(28f, -44f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-34f, -46f),
        rearFoot = p(-62f, 0f)
    )

    private fun idleInhaleFrame(): StickFrame = frame(
        head = p(0f, -158f),
        radius = 15f,
        neck = p(-2f, -142f),
        chest = p(11f, -118f),
        hips = p(-7f, -79f),
        leadShoulder = p(7f, -132f),
        leadElbow = p(37f, -118f),
        leadHand = p(51f, -102f),
        rearShoulder = p(-6f, -130f),
        rearElbow = p(-24f, -114f),
        rearHand = p(-8f, -100f),
        leadKnee = p(29f, -45f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-35f, -47f),
        rearFoot = p(-62f, 0f)
    )

    private fun idleSettleFrame(): StickFrame = frame(
        head = p(-1f, -155f),
        radius = 15f,
        neck = p(-3f, -139f),
        chest = p(9f, -115f),
        hips = p(-5f, -77f),
        leadShoulder = p(5f, -129f),
        leadElbow = p(35f, -115f),
        leadHand = p(49f, -99f),
        rearShoulder = p(-7f, -127f),
        rearElbow = p(-25f, -111f),
        rearHand = p(-9f, -97f),
        leadKnee = p(27f, -43f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-33f, -45f),
        rearFoot = p(-62f, 0f)
    )

    private fun shuffleForwardFrameA(): StickFrame = frame(
        head = p(3f, -154f),
        radius = 15f,
        neck = p(1f, -138f),
        chest = p(13f, -114f),
        hips = p(-3f, -76f),
        leadShoulder = p(9f, -128f),
        leadElbow = p(39f, -115f),
        leadHand = p(54f, -100f),
        rearShoulder = p(-3f, -126f),
        rearElbow = p(-22f, -111f),
        rearHand = p(-6f, -98f),
        leadKnee = p(32f, -41f),
        leadFoot = p(58f, 0f),
        rearKnee = p(-31f, -44f),
        rearFoot = p(-56f, 0f)
    )

    private fun shuffleForwardFrameB(): StickFrame = frame(
        head = p(2f, -152f),
        radius = 15f,
        neck = p(0f, -136f),
        chest = p(12f, -112f),
        hips = p(-2f, -74f),
        leadShoulder = p(8f, -126f),
        leadElbow = p(37f, -114f),
        leadHand = p(52f, -99f),
        rearShoulder = p(-4f, -124f),
        rearElbow = p(-23f, -109f),
        rearHand = p(-7f, -96f),
        leadKnee = p(29f, -39f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-29f, -41f),
        rearFoot = p(-50f, 0f)
    )

    private fun shuffleBackwardFrameA(): StickFrame = frame(
        head = p(-3f, -154f),
        radius = 15f,
        neck = p(-5f, -138f),
        chest = p(7f, -114f),
        hips = p(-9f, -76f),
        leadShoulder = p(3f, -128f),
        leadElbow = p(33f, -115f),
        leadHand = p(47f, -100f),
        rearShoulder = p(-9f, -126f),
        rearElbow = p(-27f, -111f),
        rearHand = p(-11f, -98f),
        leadKnee = p(24f, -41f),
        leadFoot = p(46f, 0f),
        rearKnee = p(-38f, -44f),
        rearFoot = p(-68f, 0f)
    )

    private fun shuffleBackwardFrameB(): StickFrame = frame(
        head = p(-2f, -152f),
        radius = 15f,
        neck = p(-4f, -136f),
        chest = p(8f, -112f),
        hips = p(-8f, -74f),
        leadShoulder = p(4f, -126f),
        leadElbow = p(34f, -114f),
        leadHand = p(48f, -99f),
        rearShoulder = p(-8f, -124f),
        rearElbow = p(-26f, -109f),
        rearHand = p(-10f, -96f),
        leadKnee = p(22f, -39f),
        leadFoot = p(40f, 0f),
        rearKnee = p(-34f, -41f),
        rearFoot = p(-62f, 0f)
    )

    private fun crouchFrame(): StickFrame = frame(
        head = p(-1f, -134f),
        radius = 15f,
        neck = p(-3f, -119f),
        chest = p(7f, -98f),
        hips = p(-8f, -60f),
        leadShoulder = p(4f, -109f),
        leadElbow = p(33f, -98f),
        leadHand = p(46f, -84f),
        rearShoulder = p(-8f, -108f),
        rearElbow = p(-27f, -96f),
        rearHand = p(-11f, -84f),
        leadKnee = p(34f, -25f),
        leadFoot = p(56f, 0f),
        rearKnee = p(-39f, -27f),
        rearFoot = p(-64f, 0f)
    )

    private fun jumpLoadFrame(): StickFrame = frame(
        head = p(-1f, -142f),
        radius = 15f,
        neck = p(-3f, -126f),
        chest = p(8f, -104f),
        hips = p(-8f, -65f),
        leadShoulder = p(5f, -116f),
        leadElbow = p(34f, -104f),
        leadHand = p(48f, -89f),
        rearShoulder = p(-8f, -114f),
        rearElbow = p(-27f, -101f),
        rearHand = p(-10f, -88f),
        leadKnee = p(31f, -28f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-37f, -30f),
        rearFoot = p(-62f, 0f)
    )

    private fun jumpApexFrame(): StickFrame = frame(
        head = p(2f, -205f),
        radius = 15f,
        neck = p(0f, -189f),
        chest = p(10f, -165f),
        hips = p(-4f, -126f),
        leadShoulder = p(7f, -179f),
        leadElbow = p(34f, -165f),
        leadHand = p(46f, -148f),
        rearShoulder = p(-5f, -177f),
        rearElbow = p(-24f, -160f),
        rearHand = p(-9f, -145f),
        leadKnee = p(24f, -91f),
        leadFoot = p(38f, -58f),
        rearKnee = p(-30f, -92f),
        rearFoot = p(-45f, -58f)
    )

    private fun jabLoadFrame(): StickFrame = frame(
        head = p(-2f, -155f),
        radius = 15f,
        neck = p(-4f, -139f),
        chest = p(7f, -116f),
        hips = p(-8f, -77f),
        leadShoulder = p(4f, -130f),
        leadElbow = p(31f, -116f),
        leadHand = p(42f, -100f),
        rearShoulder = p(-8f, -128f),
        rearElbow = p(-26f, -112f),
        rearHand = p(-8f, -98f),
        leadKnee = p(27f, -42f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-35f, -45f),
        rearFoot = p(-62f, 0f)
    )

    private fun jabExtendFrame(): StickFrame = frame(
        head = p(7f, -155f),
        radius = 15f,
        neck = p(5f, -139f),
        chest = p(18f, -115f),
        hips = p(-1f, -77f),
        leadShoulder = p(18f, -129f),
        leadElbow = p(61f, -115f),
        leadHand = p(96f, -101f),
        rearShoulder = p(0f, -127f),
        rearElbow = p(-21f, -112f),
        rearHand = p(-7f, -98f),
        leadKnee = p(30f, -43f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-34f, -46f),
        rearFoot = p(-62f, 0f)
    )

    private fun jabImpactFrame(): StickFrame = frame(
        head = p(9f, -155f),
        radius = 15f,
        neck = p(7f, -139f),
        chest = p(21f, -115f),
        hips = p(1f, -77f),
        leadShoulder = p(22f, -129f),
        leadElbow = p(68f, -115f),
        leadHand = p(106f, -101f),
        rearShoulder = p(1f, -127f),
        rearElbow = p(-21f, -112f),
        rearHand = p(-7f, -98f),
        leadKnee = p(31f, -44f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-34f, -46f),
        rearFoot = p(-62f, 0f)
    )

    private fun jabRecoilFrame(): StickFrame = frame(
        head = p(3f, -156f),
        radius = 15f,
        neck = p(1f, -140f),
        chest = p(13f, -116f),
        hips = p(-4f, -78f),
        leadShoulder = p(10f, -130f),
        leadElbow = p(43f, -117f),
        leadHand = p(60f, -101f),
        rearShoulder = p(-4f, -128f),
        rearElbow = p(-23f, -112f),
        rearHand = p(-8f, -98f),
        leadKnee = p(29f, -44f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-34f, -46f),
        rearFoot = p(-62f, 0f)
    )

    private fun crossLoadFrame(): StickFrame = frame(
        head = p(-3f, -155f),
        radius = 15f,
        neck = p(-5f, -139f),
        chest = p(4f, -116f),
        hips = p(-13f, -77f),
        leadShoulder = p(2f, -130f),
        leadElbow = p(31f, -116f),
        leadHand = p(47f, -100f),
        rearShoulder = p(-12f, -128f),
        rearElbow = p(-32f, -112f),
        rearHand = p(-19f, -98f),
        leadKnee = p(27f, -43f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-35f, -45f),
        rearFoot = p(-62f, 0f)
    )

    private fun crossRotateFrame(): StickFrame = frame(
        head = p(4f, -155f),
        radius = 15f,
        neck = p(2f, -139f),
        chest = p(13f, -115f),
        hips = p(-3f, -76f),
        leadShoulder = p(5f, -129f),
        leadElbow = p(29f, -115f),
        leadHand = p(20f, -99f),
        rearShoulder = p(8f, -128f),
        rearElbow = p(43f, -114f),
        rearHand = p(70f, -102f),
        leadKnee = p(31f, -44f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-29f, -45f),
        rearFoot = p(-60f, -3f)
    )

    private fun crossImpactFrame(): StickFrame = frame(
        head = p(9f, -155f),
        radius = 15f,
        neck = p(7f, -139f),
        chest = p(19f, -115f),
        hips = p(2f, -76f),
        leadShoulder = p(8f, -129f),
        leadElbow = p(28f, -115f),
        leadHand = p(18f, -99f),
        rearShoulder = p(18f, -128f),
        rearElbow = p(62f, -114f),
        rearHand = p(98f, -101f),
        leadKnee = p(32f, -44f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-26f, -45f),
        rearFoot = p(-59f, -4f)
    )

    private fun crossFollowFrame(): StickFrame = frame(
        head = p(6f, -154f),
        radius = 15f,
        neck = p(4f, -138f),
        chest = p(15f, -114f),
        hips = p(0f, -76f),
        leadShoulder = p(6f, -128f),
        leadElbow = p(28f, -114f),
        leadHand = p(24f, -98f),
        rearShoulder = p(12f, -127f),
        rearElbow = p(50f, -114f),
        rearHand = p(76f, -101f),
        leadKnee = p(31f, -43f),
        leadFoot = p(52f, 0f),
        rearKnee = p(-29f, -44f),
        rearFoot = p(-60f, 0f)
    )

    private fun heavyLoadFrame(): StickFrame = frame(
        head = p(-5f, -149f),
        radius = 15f,
        neck = p(-8f, -134f),
        chest = p(2f, -110f),
        hips = p(-15f, -70f),
        leadShoulder = p(-1f, -124f),
        leadElbow = p(23f, -110f),
        leadHand = p(32f, -95f),
        rearShoulder = p(-14f, -122f),
        rearElbow = p(-34f, -106f),
        rearHand = p(-11f, -92f),
        leadKnee = p(31f, -33f),
        leadFoot = p(54f, 0f),
        rearKnee = p(-40f, -35f),
        rearFoot = p(-66f, 0f)
    )

    private fun heavyDriveFrame(): StickFrame = frame(
        head = p(6f, -151f),
        radius = 15f,
        neck = p(4f, -136f),
        chest = p(16f, -112f),
        hips = p(-2f, -73f),
        leadShoulder = p(16f, -126f),
        leadElbow = p(55f, -113f),
        leadHand = p(84f, -100f),
        rearShoulder = p(0f, -124f),
        rearElbow = p(-20f, -108f),
        rearHand = p(-7f, -94f),
        leadKnee = p(34f, -39f),
        leadFoot = p(54f, 0f),
        rearKnee = p(-32f, -43f),
        rearFoot = p(-64f, 0f)
    )

    private fun heavyImpactFrame(): StickFrame = frame(
        head = p(13f, -152f),
        radius = 15f,
        neck = p(12f, -136f),
        chest = p(27f, -112f),
        hips = p(6f, -74f),
        leadShoulder = p(29f, -126f),
        leadElbow = p(78f, -113f),
        leadHand = p(122f, -99f),
        rearShoulder = p(7f, -124f),
        rearElbow = p(-15f, -109f),
        rearHand = p(-7f, -95f),
        leadKnee = p(36f, -45f),
        leadFoot = p(54f, 0f),
        rearKnee = p(-25f, -45f),
        rearFoot = p(-64f, -4f)
    )

    private fun heavyRecoverFrame(): StickFrame = frame(
        head = p(6f, -153f),
        radius = 15f,
        neck = p(4f, -137f),
        chest = p(16f, -114f),
        hips = p(-3f, -76f),
        leadShoulder = p(15f, -128f),
        leadElbow = p(52f, -115f),
        leadHand = p(74f, -100f),
        rearShoulder = p(0f, -126f),
        rearElbow = p(-22f, -111f),
        rearHand = p(-8f, -96f),
        leadKnee = p(33f, -42f),
        leadFoot = p(54f, 0f),
        rearKnee = p(-31f, -44f),
        rearFoot = p(-64f, 0f)
    )

    private fun hurtLightFrame(): StickFrame = frame(
        head = p(-14f, -154f),
        radius = 15f,
        neck = p(-13f, -138f),
        chest = p(-16f, -116f),
        hips = p(-9f, -78f),
        leadShoulder = p(-7f, -129f),
        leadElbow = p(18f, -113f),
        leadHand = p(31f, -96f),
        rearShoulder = p(-20f, -127f),
        rearElbow = p(-33f, -111f),
        rearHand = p(-20f, -96f),
        leadKnee = p(25f, -41f),
        leadFoot = p(48f, 0f),
        rearKnee = p(-36f, -44f),
        rearFoot = p(-64f, 0f)
    )

    private fun hurtHeavyFrame(): StickFrame = frame(
        head = p(-27f, -149f),
        radius = 15f,
        neck = p(-24f, -134f),
        chest = p(-29f, -113f),
        hips = p(-13f, -76f),
        leadShoulder = p(-20f, -126f),
        leadElbow = p(3f, -108f),
        leadHand = p(18f, -91f),
        rearShoulder = p(-32f, -124f),
        rearElbow = p(-43f, -106f),
        rearHand = p(-34f, -89f),
        leadKnee = p(22f, -37f),
        leadFoot = p(44f, 0f),
        rearKnee = p(-42f, -40f),
        rearFoot = p(-70f, 0f)
    )

    private fun fallFrame(): StickFrame = frame(
        head = p(-38f, -106f),
        radius = 15f,
        neck = p(-31f, -98f),
        chest = p(-13f, -86f),
        hips = p(5f, -56f),
        leadShoulder = p(1f, -82f),
        leadElbow = p(27f, -64f),
        leadHand = p(42f, -43f),
        rearShoulder = p(-27f, -92f),
        rearElbow = p(-52f, -72f),
        rearHand = p(-60f, -50f),
        leadKnee = p(43f, -28f),
        leadFoot = p(78f, 0f),
        rearKnee = p(-20f, -30f),
        rearFoot = p(-54f, 0f)
    )

    private fun collapsedFrame(): StickFrame = frame(
        head = p(-64f, -24f),
        radius = 15f,
        neck = p(-49f, -30f),
        chest = p(-20f, -29f),
        hips = p(25f, -20f),
        leadShoulder = p(-12f, -23f),
        leadElbow = p(17f, -10f),
        leadHand = p(42f, -4f),
        rearShoulder = p(-39f, -36f),
        rearElbow = p(-66f, -19f),
        rearHand = p(-80f, -4f),
        leadKnee = p(55f, -8f),
        leadFoot = p(90f, 0f),
        rearKnee = p(2f, -8f),
        rearFoot = p(-40f, 0f)
    )

    private fun combatIdle() = StickAnimationClip(
        id = COMBAT_IDLE,
        durationSeconds = 0.96f,
        looping = true,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.30f, idleInhaleFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.62f, idleSettleFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.96f, combatGuardFrame(), Easing.EaseInOut)
        )
    )

    private fun shuffleForward() = StickAnimationClip(
        id = SHUFFLE_FORWARD,
        durationSeconds = 0.42f,
        looping = true,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.09f, shuffleForwardFrameA(), Easing.EaseOut),
            StickAnimationFrame(0.20f, shuffleForwardFrameB(), Easing.EaseInOut),
            StickAnimationFrame(0.31f, shuffleForwardFrameA(), Easing.EaseOut),
            StickAnimationFrame(0.42f, combatGuardFrame(), Easing.EaseInOut)
        )
    )

    private fun shuffleBackward() = StickAnimationClip(
        id = SHUFFLE_BACKWARD,
        durationSeconds = 0.42f,
        looping = true,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.09f, shuffleBackwardFrameA(), Easing.EaseOut),
            StickAnimationFrame(0.20f, shuffleBackwardFrameB(), Easing.EaseInOut),
            StickAnimationFrame(0.31f, shuffleBackwardFrameA(), Easing.EaseOut),
            StickAnimationFrame(0.42f, combatGuardFrame(), Easing.EaseInOut)
        )
    )

    private fun jump() = StickAnimationClip(
        id = JUMP,
        durationSeconds = 0.78f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseIn),
            StickAnimationFrame(0.10f, jumpLoadFrame(), Easing.Snap),
            StickAnimationFrame(0.28f, jumpApexFrame(), Easing.EaseOut),
            StickAnimationFrame(0.56f, jumpLoadFrame(), Easing.EaseIn),
            StickAnimationFrame(0.78f, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun crouch() = StickAnimationClip(
        id = CROUCH,
        durationSeconds = 0.36f,
        looping = true,
        frames = listOf(
            StickAnimationFrame(0f, crouchFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.18f, crouchFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.36f, crouchFrame(), Easing.EaseInOut)
        )
    )

    private fun jab() = StickAnimationClip(
        id = JAB,
        durationSeconds = 0.42f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseIn),
            StickAnimationFrame(0.06f, jabLoadFrame(), Easing.Snap),
            StickAnimationFrame(0.12f, jabExtendFrame(), Easing.Linear),
            StickAnimationFrame(0.18f, jabImpactFrame(), Easing.EaseOut),
            StickAnimationFrame(0.28f, jabRecoilFrame(), Easing.EaseOut),
            StickAnimationFrame(0.42f, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun cross() = StickAnimationClip(
        id = CROSS,
        durationSeconds = 0.55f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseIn),
            StickAnimationFrame(0.10f, crossLoadFrame(), Easing.Snap),
            StickAnimationFrame(0.16f, crossRotateFrame(), Easing.Linear),
            StickAnimationFrame(0.22f, crossImpactFrame(), Easing.EaseOut),
            StickAnimationFrame(0.34f, crossFollowFrame(), Easing.EaseOut),
            StickAnimationFrame(0.55f, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun heavyPunch() = StickAnimationClip(
        id = HEAVY_PUNCH,
        durationSeconds = 0.72f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseIn),
            StickAnimationFrame(0.12f, heavyLoadFrame(), Easing.Snap),
            StickAnimationFrame(0.22f, heavyDriveFrame(), Easing.Linear),
            StickAnimationFrame(0.32f, heavyImpactFrame(), Easing.EaseOut),
            StickAnimationFrame(0.46f, heavyRecoverFrame(), Easing.EaseOut),
            StickAnimationFrame(0.72f, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun specialClip(id: String, durationSeconds: Float) = StickAnimationClip(
        id = id,
        durationSeconds = durationSeconds,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.EaseIn),
            StickAnimationFrame(durationSeconds * 0.20f, heavyLoadFrame(), Easing.Snap),
            StickAnimationFrame(durationSeconds * 0.42f, heavyDriveFrame(), Easing.Linear),
            StickAnimationFrame(durationSeconds * 0.66f, heavyImpactFrame(), Easing.EaseOut),
            StickAnimationFrame(durationSeconds, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun hurtLight() = StickAnimationClip(
        id = HURT_LIGHT,
        durationSeconds = 0.26f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.Snap),
            StickAnimationFrame(0.06f, hurtLightFrame(), Easing.EaseOut),
            StickAnimationFrame(0.14f, idleSettleFrame(), Easing.EaseOut),
            StickAnimationFrame(0.26f, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun hurtHeavy() = StickAnimationClip(
        id = HURT_HEAVY,
        durationSeconds = 0.42f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.Snap),
            StickAnimationFrame(0.08f, hurtHeavyFrame(), Easing.EaseOut),
            StickAnimationFrame(0.22f, hurtLightFrame(), Easing.EaseOut),
            StickAnimationFrame(0.42f, combatGuardFrame(), Easing.EaseOut)
        )
    )

    private fun knockout() = StickAnimationClip(
        id = KNOCKOUT,
        durationSeconds = 0.86f,
        looping = false,
        frames = listOf(
            StickAnimationFrame(0f, combatGuardFrame(), Easing.Snap),
            StickAnimationFrame(0.14f, hurtHeavyFrame(), Easing.EaseIn),
            StickAnimationFrame(0.38f, fallFrame(), Easing.EaseInOut),
            StickAnimationFrame(0.86f, collapsedFrame(), Easing.EaseOut)
        )
    )

    private fun frame(
        head: Vec2,
        radius: Float,
        neck: Vec2,
        chest: Vec2,
        hips: Vec2,
        leadShoulder: Vec2,
        leadElbow: Vec2,
        leadHand: Vec2,
        rearShoulder: Vec2,
        rearElbow: Vec2,
        rearHand: Vec2,
        leadKnee: Vec2,
        leadFoot: Vec2,
        rearKnee: Vec2,
        rearFoot: Vec2
    ): StickFrame {
        return StickFrame(
            head = StickCircle(head, radius),
            torso = listOf(StickSegment(neck, chest), StickSegment(chest, hips)),
            leadUpperArm = StickSegment(leadShoulder, leadElbow),
            leadForearm = StickSegment(leadElbow, leadHand),
            rearUpperArm = StickSegment(rearShoulder, rearElbow),
            rearForearm = StickSegment(rearElbow, rearHand),
            leadThigh = StickSegment(hips, leadKnee),
            leadShin = StickSegment(leadKnee, leadFoot),
            rearThigh = StickSegment(hips, rearKnee),
            rearShin = StickSegment(rearKnee, rearFoot),
            eye = head + Vec2(6f, -2f)
        )
    }

    private fun p(x: Float, y: Float) = Vec2(x, y)
}

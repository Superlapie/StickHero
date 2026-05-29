package com.stickhero.game.animation

import com.stickhero.game.physics.Vec2

object PoseLibrary {
    const val IDLE_BREATHE = "idle_breathe"
    const val WALK_FORWARD = "walk_forward"
    const val JUMP = "jump"
    const val CROUCH = "crouch"
    const val JAB = "jab"
    const val HEAVY_PUNCH = "heavy_punch"
    const val HURT_LIGHT = "hurt_light"
    const val HURT_HEAVY = "hurt_heavy"
    const val KNOCKOUT = "knockout"

    private val clips = listOf(
        idleBreathe(),
        walkForward(),
        jump(),
        crouchHold(),
        jab(),
        heavyPunch(),
        hurtLight(),
        hurtHeavy(),
        knockout()
    ).associateBy { it.id }

    fun clip(id: String): AnimationClip = clips[id] ?: clips.getValue(IDLE_BREATHE)

    fun basePose(
        bob: Float = 0f,
        lean: Float = 0f,
        chestX: Float = 4f,
        hipsX: Float = -4f,
        leadArmX: Float = 28f,
        leadArmY: Float = -88f,
        rearArmX: Float = -12f,
        rearArmY: Float = -94f,
        leadLegX: Float = 24f,
        rearLegX: Float = -18f,
        kneeBend: Float = 12f,
        headX: Float = 4f,
        headY: Float = -168f
    ): Pose {
        val hips = Vec2(hipsX, -66f + bob)
        val chest = Vec2(chestX + lean * 0.45f, -122f + bob)
        val neck = Vec2(chest.x + lean * 0.6f, -150f + bob)
        return Pose(
            mapOf(
                JointId.Head to JointTransform(Vec2(headX + lean * 0.7f, headY + bob)),
                JointId.Neck to JointTransform(neck),
                JointId.Chest to JointTransform(chest),
                JointId.Hips to JointTransform(hips),
                JointId.LeftShoulder to JointTransform(Vec2(neck.x - 18f, -138f + bob)),
                JointId.LeftElbow to JointTransform(Vec2(rearArmX * 0.62f - 12f, rearArmY - 2f + bob)),
                JointId.LeftHand to JointTransform(Vec2(rearArmX, rearArmY + bob)),
                JointId.RightShoulder to JointTransform(Vec2(neck.x + 18f, -136f + bob)),
                JointId.RightElbow to JointTransform(Vec2(leadArmX * 0.62f + 12f, leadArmY - 2f + bob)),
                JointId.RightHand to JointTransform(Vec2(leadArmX, leadArmY + bob)),
                JointId.LeftKnee to JointTransform(Vec2(rearLegX * 0.62f, -44f + kneeBend + bob)),
                JointId.LeftFoot to JointTransform(Vec2(rearLegX, 0f)),
                JointId.RightKnee to JointTransform(Vec2(leadLegX * 0.62f, -44f + kneeBend + bob)),
                JointId.RightFoot to JointTransform(Vec2(leadLegX, 0f))
            )
        )
    }

    fun crouchPose(amount: Float): Pose {
        val t = amount.coerceIn(0f, 1f)
        return Pose.interpolate(basePose(), crouchedBasePose(), t)
    }

    private fun crouchedBasePose(): Pose {
        return basePose(
            bob = 30f,
            lean = 18f,
            chestX = 2f,
            hipsX = -4f,
            leadArmX = 20f,
            leadArmY = -82f,
            rearArmX = -8f,
            rearArmY = -88f,
            leadLegX = 24f,
            rearLegX = -18f,
            kneeBend = 44f,
            headX = 2f,
            headY = -140f
        )
    }

    private fun idleBreathe() = AnimationClip(
        id = IDLE_BREATHE,
        durationSeconds = 1.25f,
        looping = true,
        keyframes = listOf(
            Keyframe(0f, basePose(bob = 0f, lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 30f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 26f, rearLegX = -20f), Easing.EaseInOut),
            Keyframe(0.62f, basePose(bob = -4f, lean = 12f, chestX = 12f, hipsX = -10f, leadArmX = 32f, leadArmY = -88f, rearArmX = -6f, rearArmY = -98f, leadLegX = 27f, rearLegX = -21f), Easing.EaseInOut),
            Keyframe(1.25f, basePose(bob = 0f, lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 30f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 26f, rearLegX = -20f), Easing.EaseInOut)
        )
    )

    private fun walkForward() = AnimationClip(
        id = WALK_FORWARD,
        durationSeconds = 0.48f,
        looping = true,
        keyframes = listOf(
            Keyframe(0f, basePose(bob = 0f, lean = 10f, chestX = 11f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 25f, rearLegX = -19f), Easing.EaseInOut),
            Keyframe(0.12f, basePose(bob = -5f, lean = 14f, chestX = 13f, hipsX = -10f, leadArmX = 32f, leadArmY = -88f, rearArmX = -6f, rearArmY = -100f, leadLegX = 21f, rearLegX = -16f, kneeBend = 24f), Easing.EaseInOut),
            Keyframe(0.24f, basePose(bob = 1f, lean = 7f, chestX = 8f, hipsX = -5f, leadArmX = 36f, leadArmY = -82f, rearArmX = -10f, rearArmY = -92f, leadLegX = 28f, rearLegX = -23f), Easing.EaseInOut),
            Keyframe(0.36f, basePose(bob = -5f, lean = 13f, chestX = 12f, hipsX = -9f, leadArmX = 30f, leadArmY = -87f, rearArmX = -7f, rearArmY = -98f, leadLegX = 22f, rearLegX = -17f, kneeBend = 24f), Easing.EaseInOut),
            Keyframe(0.48f, basePose(bob = 0f, lean = 10f, chestX = 11f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 25f, rearLegX = -19f), Easing.EaseInOut)
        )
    )

    private fun jump() = AnimationClip(
        id = JUMP,
        durationSeconds = 0.72f,
        looping = false,
        keyframes = listOf(
            Keyframe(0f, basePose(bob = -1f, lean = 8f, chestX = 10f, hipsX = -8f, leadArmX = 28f, leadArmY = -84f, rearArmX = -8f, rearArmY = -94f, leadLegX = 24f, rearLegX = -19f, kneeBend = 20f), Easing.EaseOut),
            Keyframe(0.09f, basePose(bob = -16f, lean = 2f, chestX = 5f, hipsX = -4f, leadArmX = 26f, leadArmY = -98f, rearArmX = -10f, rearArmY = -102f, leadLegX = 18f, rearLegX = -14f, kneeBend = 34f, headY = -154f), Easing.EaseOut),
            Keyframe(0.24f, basePose(bob = -30f, lean = 0f, chestX = 3f, hipsX = -2f, leadArmX = 22f, leadArmY = -114f, rearArmX = -12f, rearArmY = -114f, leadLegX = 14f, rearLegX = -10f, kneeBend = 18f, headY = -170f), Easing.EaseInOut),
            Keyframe(0.42f, basePose(bob = -22f, lean = 4f, chestX = 6f, hipsX = -5f, leadArmX = 24f, leadArmY = -104f, rearArmX = -10f, rearArmY = -104f, leadLegX = 20f, rearLegX = -16f, kneeBend = 10f, headY = -162f), Easing.EaseInOut),
            Keyframe(0.58f, basePose(bob = -8f, lean = 8f, chestX = 9f, hipsX = -7f, leadArmX = 26f, leadArmY = -92f, rearArmX = -8f, rearArmY = -96f, leadLegX = 23f, rearLegX = -18f, kneeBend = 8f, headY = -150f), Easing.EaseOut),
            Keyframe(0.72f, basePose(bob = 0f, lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 24f, rearLegX = -19f), Easing.EaseOut)
        )
    )

    private fun crouchHold() = AnimationClip(
        id = CROUCH,
        durationSeconds = 0.24f,
        looping = true,
        keyframes = listOf(
            Keyframe(0f, crouchPose(0.1f), Easing.EaseInOut),
            Keyframe(0.12f, crouchPose(0.9f), Easing.EaseInOut),
            Keyframe(0.24f, crouchPose(0.1f), Easing.EaseInOut)
        )
    )

    private fun jab() = AnimationClip(
        id = JAB,
        durationSeconds = 0.49f,
        looping = false,
        keyframes = listOf(
            Keyframe(0f, basePose(lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 24f, rearLegX = -19f), Easing.EaseOut),
            Keyframe(0.08f, basePose(lean = 2f, chestX = 4f, hipsX = -4f, leadArmX = 20f, leadArmY = -92f, rearArmX = -10f, rearArmY = -100f, leadLegX = 23f, rearLegX = -18f, kneeBend = 16f), Easing.Snap),
            Keyframe(0.16f, basePose(lean = 18f, chestX = 18f, hipsX = -12f, leadArmX = 150f, leadArmY = -90f, rearArmX = -14f, rearArmY = -100f, leadLegX = 28f, rearLegX = -22f, kneeBend = 10f), Easing.Linear),
            Keyframe(0.24f, basePose(lean = 12f, chestX = 12f, hipsX = -9f, leadArmX = 122f, leadArmY = -88f, rearArmX = -10f, rearArmY = -98f, leadLegX = 26f, rearLegX = -20f, kneeBend = 12f), Easing.EaseOut),
            Keyframe(0.49f, basePose(lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 24f, rearLegX = -19f), Easing.EaseOut)
        )
    )

    private fun heavyPunch() = AnimationClip(
        id = HEAVY_PUNCH,
        durationSeconds = 0.72f,
        looping = false,
        keyframes = listOf(
            Keyframe(0f, basePose(lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 24f, rearLegX = -19f), Easing.EaseInOut),
            Keyframe(0.16f, basePose(lean = 0f, chestX = 4f, hipsX = -5f, leadArmX = 18f, leadArmY = -94f, rearArmX = -10f, rearArmY = -100f, leadLegX = 23f, rearLegX = -18f, kneeBend = 16f), Easing.Snap),
            Keyframe(0.30f, basePose(lean = 24f, chestX = 22f, hipsX = -14f, leadArmX = 164f, leadArmY = -92f, rearArmX = -14f, rearArmY = -102f, leadLegX = 30f, rearLegX = -24f, kneeBend = 8f), Easing.Linear),
            Keyframe(0.42f, basePose(lean = 16f, chestX = 16f, hipsX = -10f, leadArmX = 132f, leadArmY = -90f, rearArmX = -10f, rearArmY = -98f, leadLegX = 28f, rearLegX = -22f, kneeBend = 10f), Easing.EaseOut),
            Keyframe(0.72f, basePose(lean = 10f, chestX = 10f, hipsX = -8f, leadArmX = 28f, leadArmY = -86f, rearArmX = -8f, rearArmY = -96f, leadLegX = 24f, rearLegX = -19f), Easing.EaseOut)
        )
    )

    private fun hurtLight() = AnimationClip(
        id = HURT_LIGHT,
        durationSeconds = 0.24f,
        looping = false,
        keyframes = listOf(
            Keyframe(0f, basePose(), Easing.Snap),
            Keyframe(0.06f, basePose(lean = -20f, chestX = -13f, hipsX = -4f, headX = -13f, leadArmX = 12f, rearArmX = -15f), Easing.EaseOut),
            Keyframe(0.24f, basePose(), Easing.EaseOut)
        )
    )

    private fun hurtHeavy() = AnimationClip(
        id = HURT_HEAVY,
        durationSeconds = 0.38f,
        looping = false,
        keyframes = listOf(
            Keyframe(0f, basePose(), Easing.Snap),
            Keyframe(0.08f, basePose(lean = -34f, chestX = -22f, hipsX = -8f, headX = -22f, headY = -132f, leadArmX = 2f, rearArmX = -8f, leadLegX = 13f, rearLegX = -33f), Easing.EaseOut),
            Keyframe(0.38f, basePose(), Easing.EaseOut)
        )
    )

    private fun knockout() = AnimationClip(
        id = KNOCKOUT,
        durationSeconds = 0.75f,
        looping = false,
        keyframes = listOf(
            Keyframe(0f, basePose(), Easing.Snap),
            Keyframe(0.18f, basePose(lean = -42f, chestX = -28f, hipsX = -12f, headX = -32f, headY = -118f, leadLegX = 11f, rearLegX = -39f), Easing.EaseIn),
            Keyframe(0.75f, collapsedPose(), Easing.EaseOut)
        )
    )

    private fun collapsedPose(): Pose = Pose(
        mapOf(
            JointId.Head to JointTransform(Vec2(-53f, -24f)),
            JointId.Neck to JointTransform(Vec2(-40f, -32f)),
            JointId.Chest to JointTransform(Vec2(-18f, -28f)),
            JointId.Hips to JointTransform(Vec2(20f, -21f)),
            JointId.LeftShoulder to JointTransform(Vec2(-34f, -35f)),
            JointId.LeftElbow to JointTransform(Vec2(-55f, -18f)),
            JointId.LeftHand to JointTransform(Vec2(-69f, -4f)),
            JointId.RightShoulder to JointTransform(Vec2(-22f, -20f)),
            JointId.RightElbow to JointTransform(Vec2(0f, -7f)),
            JointId.RightHand to JointTransform(Vec2(22f, -3f)),
            JointId.LeftKnee to JointTransform(Vec2(6f, -8f)),
            JointId.LeftFoot to JointTransform(Vec2(-28f, 0f)),
            JointId.RightKnee to JointTransform(Vec2(45f, -8f)),
            JointId.RightFoot to JointTransform(Vec2(78f, 0f))
        )
    )
}

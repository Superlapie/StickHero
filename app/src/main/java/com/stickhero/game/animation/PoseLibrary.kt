package com.stickhero.game.animation

object PoseLibrary {
    const val COMBAT_IDLE = StickAnimationLibrary.COMBAT_IDLE
    const val IDLE_BREATHE = COMBAT_IDLE
    const val WALK_FORWARD = StickAnimationLibrary.SHUFFLE_FORWARD
    const val WALK_BACKWARD = StickAnimationLibrary.SHUFFLE_BACKWARD
    const val JUMP = StickAnimationLibrary.JUMP
    const val CROUCH = StickAnimationLibrary.CROUCH
    const val JAB = StickAnimationLibrary.JAB
    const val CROSS = StickAnimationLibrary.CROSS
    const val HEAVY_PUNCH = StickAnimationLibrary.HEAVY_PUNCH
    const val HURT_LIGHT = StickAnimationLibrary.HURT_LIGHT
    const val HURT_HEAVY = StickAnimationLibrary.HURT_HEAVY
    const val KNOCKOUT = StickAnimationLibrary.KNOCKOUT

    fun basePose(): Pose = poseFromFrame(StickAnimationLibrary.combatGuardFrame())

    fun crouchPose(amount: Float): Pose {
        amount.coerceIn(0f, 1f)
        return poseFromFrame(StickAnimationLibrary.clip(CROUCH).frames.first().frame)
    }

    private fun poseFromFrame(frame: StickFrame): Pose {
        val torso = frame.torso
        val neck = torso.first().from
        val chest = torso.first().to
        val hips = torso.last().to
        return Pose(
            mapOf(
                JointId.Head to JointTransform(frame.head.center),
                JointId.Neck to JointTransform(neck),
                JointId.Chest to JointTransform(chest),
                JointId.Hips to JointTransform(hips),
                JointId.LeftShoulder to JointTransform(frame.rearUpperArm.from),
                JointId.LeftElbow to JointTransform(frame.rearUpperArm.to),
                JointId.LeftHand to JointTransform(frame.rearForearm.to),
                JointId.RightShoulder to JointTransform(frame.leadUpperArm.from),
                JointId.RightElbow to JointTransform(frame.leadUpperArm.to),
                JointId.RightHand to JointTransform(frame.leadForearm.to),
                JointId.LeftKnee to JointTransform(frame.rearThigh.to),
                JointId.LeftFoot to JointTransform(frame.rearShin.to),
                JointId.RightKnee to JointTransform(frame.leadThigh.to),
                JointId.RightFoot to JointTransform(frame.leadShin.to)
            )
        )
    }
}

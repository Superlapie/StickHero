package com.stickhero.game.animation

data class Bone(val from: JointId, val to: JointId)

object Skeleton {
    val bones = listOf(
        Bone(JointId.Neck, JointId.Chest),
        Bone(JointId.Chest, JointId.Hips),
        Bone(JointId.Neck, JointId.LeftShoulder),
        Bone(JointId.LeftShoulder, JointId.LeftElbow),
        Bone(JointId.LeftElbow, JointId.LeftHand),
        Bone(JointId.Neck, JointId.RightShoulder),
        Bone(JointId.RightShoulder, JointId.RightElbow),
        Bone(JointId.RightElbow, JointId.RightHand),
        Bone(JointId.Hips, JointId.LeftKnee),
        Bone(JointId.LeftKnee, JointId.LeftFoot),
        Bone(JointId.Hips, JointId.RightKnee),
        Bone(JointId.RightKnee, JointId.RightFoot)
    )
}

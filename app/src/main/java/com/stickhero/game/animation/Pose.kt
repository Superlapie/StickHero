package com.stickhero.game.animation

import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.physics.Vec2

data class Pose(
    val joints: Map<JointId, JointTransform>
) {
    operator fun get(jointId: JointId): JointTransform = joints.getValue(jointId)

    fun worldPosition(jointId: JointId, origin: Vec2, facing: FacingDirection): Vec2 {
        val local = this[jointId].position
        val direction = if (facing == FacingDirection.Right) 1f else -1f
        return Vec2(origin.x + local.x * direction, origin.y + local.y)
    }

    companion object {
        fun interpolate(from: Pose, to: Pose, t: Float): Pose {
            val ids = JointId.values()
            return Pose(ids.associateWith { id -> from[id].lerp(to[id], t) })
        }
    }
}

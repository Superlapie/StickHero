package com.stickhero.game.animation

object AnimationBlend {
    fun blend(from: Pose, to: Pose, amount: Float): Pose {
        return Pose.interpolate(from, to, amount.coerceIn(0f, 1f))
    }
}

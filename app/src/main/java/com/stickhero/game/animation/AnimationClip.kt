package com.stickhero.game.animation

data class Keyframe(
    val timeSeconds: Float,
    val pose: Pose,
    val easingToNext: Easing = Easing.EaseInOut
)

data class AnimationClip(
    val id: String,
    val durationSeconds: Float,
    val looping: Boolean,
    val keyframes: List<Keyframe>
)

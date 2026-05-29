package com.stickhero.game.animation

class AnimationPlayer {
    fun advanceTime(current: Float, clip: AnimationClip, deltaSeconds: Float): Float {
        val next = current + deltaSeconds
        return if (clip.looping && clip.durationSeconds > 0f) {
            next % clip.durationSeconds
        } else {
            next.coerceAtMost(clip.durationSeconds)
        }
    }

    fun sample(clip: AnimationClip, timeSeconds: Float): Pose {
        if (clip.keyframes.isEmpty()) return PoseLibrary.basePose()
        if (clip.keyframes.size == 1) return clip.keyframes.first().pose
        val t = if (clip.looping && clip.durationSeconds > 0f) {
            timeSeconds % clip.durationSeconds
        } else {
            timeSeconds.coerceIn(0f, clip.durationSeconds)
        }
        val frames = clip.keyframes
        val fromIndex = frames.indexOfLast { it.timeSeconds <= t }.coerceAtLeast(0)
        val from = frames[fromIndex]
        val to = frames.getOrNull(fromIndex + 1) ?: frames.last()
        if (from === to || to.timeSeconds <= from.timeSeconds) return from.pose
        val localT = ((t - from.timeSeconds) / (to.timeSeconds - from.timeSeconds)).coerceIn(0f, 1f)
        return Pose.interpolate(from.pose, to.pose, from.easingToNext.apply(localT))
    }
}

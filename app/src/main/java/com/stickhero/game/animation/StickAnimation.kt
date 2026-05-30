package com.stickhero.game.animation

import com.stickhero.game.physics.Vec2

data class StickSegment(
    val from: Vec2,
    val to: Vec2
)

data class StickCircle(
    val center: Vec2,
    val radius: Float
)

data class StickFrame(
    val head: StickCircle,
    val torso: List<StickSegment>,
    val leadUpperArm: StickSegment,
    val leadForearm: StickSegment,
    val rearUpperArm: StickSegment,
    val rearForearm: StickSegment,
    val leadThigh: StickSegment,
    val leadShin: StickSegment,
    val rearThigh: StickSegment,
    val rearShin: StickSegment,
    val eye: Vec2? = null,
    val rootOffset: Vec2 = Vec2()
) {
    val leadHand: Vec2 get() = leadForearm.to
}

data class StickAnimationFrame(
    val timeSeconds: Float,
    val frame: StickFrame,
    val easingToNext: Easing = Easing.EaseInOut
)

data class StickAnimationClip(
    val id: String,
    val durationSeconds: Float,
    val looping: Boolean,
    val frames: List<StickAnimationFrame>
)

class StickAnimationPlayer {
    fun advanceTime(current: Float, clip: StickAnimationClip, deltaSeconds: Float): Float {
        val next = current + deltaSeconds
        return if (clip.looping && clip.durationSeconds > 0f) {
            next % clip.durationSeconds
        } else {
            next.coerceAtMost(clip.durationSeconds)
        }
    }

    fun sample(clip: StickAnimationClip, timeSeconds: Float): StickFrame {
        if (clip.frames.isEmpty()) return StickAnimationLibrary.combatGuardFrame()
        if (clip.frames.size == 1) return clip.frames.first().frame
        val t = if (clip.looping && clip.durationSeconds > 0f) {
            timeSeconds % clip.durationSeconds
        } else {
            timeSeconds.coerceIn(0f, clip.durationSeconds)
        }
        val frames = clip.frames
        val fromIndex = frames.indexOfLast { it.timeSeconds <= t }.coerceAtLeast(0)
        val from = frames[fromIndex]
        val to = frames.getOrNull(fromIndex + 1) ?: frames.last()
        if (from === to || to.timeSeconds <= from.timeSeconds) return from.frame
        val localT = ((t - from.timeSeconds) / (to.timeSeconds - from.timeSeconds)).coerceIn(0f, 1f)
        return StickFrameInterpolator.interpolate(from.frame, to.frame, from.easingToNext.apply(localT))
    }
}

private object StickFrameInterpolator {
    fun interpolate(from: StickFrame, to: StickFrame, t: Float): StickFrame {
        return StickFrame(
            head = StickCircle(from.head.center.lerp(to.head.center, t), lerp(from.head.radius, to.head.radius, t)),
            torso = from.torso.zip(to.torso).map { (a, b) -> segment(a, b, t) },
            leadUpperArm = segment(from.leadUpperArm, to.leadUpperArm, t),
            leadForearm = segment(from.leadForearm, to.leadForearm, t),
            rearUpperArm = segment(from.rearUpperArm, to.rearUpperArm, t),
            rearForearm = segment(from.rearForearm, to.rearForearm, t),
            leadThigh = segment(from.leadThigh, to.leadThigh, t),
            leadShin = segment(from.leadShin, to.leadShin, t),
            rearThigh = segment(from.rearThigh, to.rearThigh, t),
            rearShin = segment(from.rearShin, to.rearShin, t),
            eye = from.eye?.let { a -> to.eye?.let { b -> a.lerp(b, t) } },
            rootOffset = from.rootOffset.lerp(to.rootOffset, t)
        )
    }

    private fun segment(from: StickSegment, to: StickSegment, t: Float): StickSegment {
        return StickSegment(from.from.lerp(to.from, t), from.to.lerp(to.to, t))
    }

    private fun lerp(from: Float, to: Float, t: Float): Float = from + (to - from) * t

    private fun Vec2.lerp(to: Vec2, t: Float): Vec2 {
        return Vec2(lerp(x, to.x, t), lerp(y, to.y, t))
    }
}

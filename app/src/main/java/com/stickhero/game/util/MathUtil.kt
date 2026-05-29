package com.stickhero.game.util

object MathUtil {
    fun approach(current: Float, target: Float, maxDelta: Float): Float = when {
        current < target -> (current + maxDelta).coerceAtMost(target)
        current > target -> (current - maxDelta).coerceAtLeast(target)
        else -> target
    }
}

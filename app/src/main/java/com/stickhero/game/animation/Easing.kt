package com.stickhero.game.animation

import kotlin.math.pow

enum class Easing {
    Linear,
    EaseIn,
    EaseOut,
    EaseInOut,
    Snap
}

fun Easing.apply(t: Float): Float {
    val x = t.coerceIn(0f, 1f)
    return when (this) {
        Easing.Linear -> x
        Easing.EaseIn -> x * x
        Easing.EaseOut -> 1f - (1f - x) * (1f - x)
        Easing.EaseInOut -> if (x < 0.5f) 2f * x * x else 1f - (-2f * x + 2f).pow(2) / 2f
        Easing.Snap -> if (x < 0.7f) (x / 0.7f).pow(3) * 0.55f else 0.55f + ((x - 0.7f) / 0.3f) * 0.45f
    }
}

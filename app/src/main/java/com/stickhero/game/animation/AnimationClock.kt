package com.stickhero.game.animation

class AnimationClock {
    fun advance(current: Float, deltaSeconds: Float): Float = current + deltaSeconds
}

package com.stickhero.game.util

object Time {
    fun secondsFromNanos(nanos: Long): Float = nanos / 1_000_000_000f
}

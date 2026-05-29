package com.stickhero.game.combat

sealed class HitResult {
    data object Miss : HitResult()
    data class Hit(val event: DamageEvent) : HitResult()
}

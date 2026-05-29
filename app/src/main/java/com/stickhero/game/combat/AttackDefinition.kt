package com.stickhero.game.combat

data class AttackDefinition(
    val id: String,
    val animationClipId: String,
    val damage: Int,
    val range: Float,
    val startupDuration: Float,
    val activeDuration: Float,
    val recoveryDuration: Float,
    val knockback: Knockback,
    val hitstunDuration: Float,
    val hitstopDuration: Float = 0.045f,
    val impactShake: Float = 0.5f
) {
    val totalDuration: Float = startupDuration + activeDuration + recoveryDuration
}

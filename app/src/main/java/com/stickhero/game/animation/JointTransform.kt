package com.stickhero.game.animation

import com.stickhero.game.physics.Vec2

data class JointTransform(
    val position: Vec2,
    val rotationRadians: Float = 0f
) {
    fun lerp(to: JointTransform, t: Float): JointTransform {
        return JointTransform(
            position = Vec2(
                x = position.x + (to.position.x - position.x) * t,
                y = position.y + (to.position.y - position.y) * t
            ),
            rotationRadians = rotationRadians + (to.rotationRadians - rotationRadians) * t
        )
    }
}

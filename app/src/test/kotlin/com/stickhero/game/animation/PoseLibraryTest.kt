package com.stickhero.game.animation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PoseLibraryTest {
    @Test
    fun walkForwardKeepsLimbsOnTheirOwnSide() {
        val clip = PoseLibrary.clip(PoseLibrary.WALK_FORWARD)

        clip.keyframes.forEach { keyframe ->
            val pose = keyframe.pose
            assertTrue(
                pose[JointId.RightHand].position.x >= 0f,
                "right hand crossed center at ${keyframe.timeSeconds}s"
            )
            assertTrue(
                pose[JointId.LeftHand].position.x <= 0f,
                "left hand crossed center at ${keyframe.timeSeconds}s"
            )
            assertTrue(
                pose[JointId.RightFoot].position.x >= 0f,
                "right foot crossed center at ${keyframe.timeSeconds}s"
            )
            assertTrue(
                pose[JointId.LeftFoot].position.x <= 0f,
                "left foot crossed center at ${keyframe.timeSeconds}s"
            )
        }
    }

    @Test
    fun crouchPoseDropsTheBodyAndKeepsGuardUp() {
        val standing = PoseLibrary.crouchPose(0f)
        val crouched = PoseLibrary.crouchPose(1f)

        assertTrue(crouched[JointId.Hips].position.y > standing[JointId.Hips].position.y)
        assertTrue(crouched[JointId.Head].position.y > standing[JointId.Head].position.y)
        assertTrue(crouched[JointId.RightHand].position.y > standing[JointId.RightHand].position.y)
        assertTrue(crouched[JointId.RightHand].position.y < 0f)
    }

    @Test
    fun jumpClipContainsApexAndLandingRecovery() {
        val clip = PoseLibrary.clip(PoseLibrary.JUMP)

        assertEquals(PoseLibrary.JUMP, clip.id)
        assertTrue(clip.keyframes.size >= 5)
        assertTrue(clip.keyframes[2].pose[JointId.Head].position.y < clip.keyframes[0].pose[JointId.Head].position.y)
        assertTrue(clip.keyframes.last().pose[JointId.RightFoot].position.x > 0f)
    }
}

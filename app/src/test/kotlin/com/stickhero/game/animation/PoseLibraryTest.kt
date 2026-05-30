package com.stickhero.game.animation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PoseLibraryTest {
    @Test
    fun combatGuardFrameUsesAuthoredFighterSilhouette() {
        val frame = StickAnimationLibrary.combatGuardFrame()

        assertEquals(0f, frame.head.center.x)
        assertEquals(-156f, frame.head.center.y)
        assertEquals(15f, frame.head.radius)
        assertEquals(50f, frame.leadForearm.to.x)
        assertEquals(-8f, frame.rearForearm.to.x)
        assertEquals(52f, frame.leadShin.to.x)
        assertEquals(-62f, frame.rearShin.to.x)
        assertTrue(frame.leadThigh.to.y < -35f)
        assertTrue(frame.rearThigh.to.y < -35f)
    }

    @Test
    fun authoredPunchesKeepGuardLogicAndDistinctStrikingShapes() {
        val jabImpact = StickAnimationLibrary.clip(StickAnimationLibrary.JAB).frames.first { it.timeSeconds == 0.18f }.frame
        val crossImpact = StickAnimationLibrary.clip(StickAnimationLibrary.CROSS).frames.first { it.timeSeconds == 0.22f }.frame
        val heavyImpact = StickAnimationLibrary.clip(StickAnimationLibrary.HEAVY_PUNCH).frames.first { it.timeSeconds == 0.32f }.frame
        val guard = StickAnimationLibrary.combatGuardFrame()

        assertTrue(jabImpact.leadHand.x >= 100f)
        assertTrue(jabImpact.torso.first().to.x > guard.torso.first().to.x)
        assertTrue(jabImpact.rearForearm.to.y < -90f)

        assertTrue(crossImpact.rearForearm.to.x >= 90f)
        assertTrue(crossImpact.leadForearm.to.y < -90f)

        assertTrue(heavyImpact.leadHand.x >= 115f)
        assertTrue(heavyImpact.leadHand.x > jabImpact.leadHand.x)
        assertTrue(heavyImpact.rearShin.to.x < 0f)
        assertTrue(heavyImpact.leadShin.to.x > 0f)
    }

    @Test
    fun shuffleForwardKeepsHandsUpAndFeetPlanted() {
        val clip = StickAnimationLibrary.clip(StickAnimationLibrary.SHUFFLE_FORWARD)

        clip.frames.forEach { frame ->
            assertTrue(frame.frame.leadHand.y < -90f)
            assertTrue(frame.frame.rearForearm.to.y < -90f)
            assertTrue(frame.frame.leadShin.to.x > 0f)
            assertTrue(frame.frame.rearShin.to.x < 0f)
            assertEquals(0f, frame.frame.leadShin.to.y)
            assertEquals(0f, frame.frame.rearShin.to.y)
        }
    }

    @Test
    fun jumpClipContainsApexAndLandingRecovery() {
        val clip = StickAnimationLibrary.clip(StickAnimationLibrary.JUMP)

        assertEquals(StickAnimationLibrary.JUMP, clip.id)
        assertTrue(clip.frames.size >= 5)
        assertTrue(clip.frames[2].frame.head.center.y < clip.frames[0].frame.head.center.y)
        assertTrue(clip.frames.last().frame.leadShin.to.x > 0f)
    }
}

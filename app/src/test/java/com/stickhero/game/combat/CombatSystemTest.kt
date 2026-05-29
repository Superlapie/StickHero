package com.stickhero.game.combat

import com.stickhero.game.config.AttackCatalog
import com.stickhero.game.config.FighterCatalog
import com.stickhero.game.input.GameCommand
import com.stickhero.game.input.InputState
import kotlin.test.Test
import kotlin.test.assertEquals

class CombatSystemTest {
    @Test
    fun attackOnlyHitsTargetOnce() {
        val combatSystem = CombatSystem(AttackCatalog.basicPunch)
        val player = FighterCatalog.player(100f, 520f)
        val enemy = FighterCatalog.enemy(150f, 520f)
        val attackInput = InputState(setOf(GameCommand.Attack))

        val first = combatSystem.update(player, enemy, attackInput, 0.13f)
        val second = combatSystem.update(player, enemy, attackInput, 0.04f)
        val third = combatSystem.update(player, enemy, attackInput, 0.04f)

        assertEquals(1, first.size + second.size + third.size)
    }

    @Test
    fun recoveryBlocksImmediateSecondAttack() {
        val combatSystem = CombatSystem(AttackCatalog.basicPunch)
        val player = FighterCatalog.player(100f, 520f)
        val enemy = FighterCatalog.enemy(150f, 520f)
        val attackInput = InputState(setOf(GameCommand.Attack))

        combatSystem.update(player, enemy, attackInput, 0.13f)
        combatSystem.update(player, enemy, attackInput, 0.10f)

        assertEquals("basic_punch", player.runtime.activeAttack?.definition?.id)
    }
}

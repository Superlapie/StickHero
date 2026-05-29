package com.stickhero.game.ai

import com.stickhero.game.fighter.Fighter

interface FighterAI {
    fun decide(self: Fighter, opponent: Fighter): AICommand
}

package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.PlayerId

interface Evaluator {

    fun evaluate(
        state: GameState,
        player: PlayerId
    ): Double
}
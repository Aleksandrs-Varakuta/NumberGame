package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.PlayerId

class SimpleEvaluator : Evaluator {

    override fun evaluate(
        state: GameState,
        player: PlayerId
    ): Double {

        val score =
            state.firstPlayerScore - state.secondPlayerScore

        return if (player == PlayerId.FIRST)
            score.toDouble()
        else
            -score.toDouble()
    }
}
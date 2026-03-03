package com.rtu.number.game.logic.evaluation

import com.rtu.number.game.logic.coreLogic.GameState

interface Evaluator {
    fun evaluate(state: GameState, player: Int): Double
    fun getName(): String = this::class.simpleName ?: "Evaluator"
}

package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.GameStatus

class GameNode(
    val state: GameState,
    val move: Move? = null,
    val depth: Int = 0
) {

    val children = mutableListOf<GameNode>()

    var evaluation: Double = 0.0

    fun isTerminal(): Boolean {
        return state.status is GameStatus.Finished
    }
}
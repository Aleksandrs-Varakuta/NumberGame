package com.rtu.number.game.domain.engine

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.rules.NumberGameRules
import javax.inject.Inject

class GameEngine @Inject constructor(
    private val rules: NumberGameRules
) {

    fun getAvailableMoves(state: GameState): List<Move> {
        return rules.getAvailableMoves(state)
    }

    fun applyMove(state: GameState, move: Move): GameState {
        return rules.applyMove(state, move)
    }

    fun getStatus(state: GameState) = rules.getStatus(state)
}
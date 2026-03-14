package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import javax.inject.Inject

interface MoveValidator {
    fun isValid(state: GameState, move: Move): Boolean

    fun requireValid(state: GameState, move: Move)
}

class DefaultMoveValidator @Inject constructor() : MoveValidator {

    override fun isValid(state: GameState, move: Move): Boolean {
        return move.leftIndex >= 0 && move.rightIndex < state.numbers.size
    }

    override fun requireValid(state: GameState, move: Move) {
        require(isValid(state, move)) {
            "Move is invalid for current state. leftIndex=${move.leftIndex}, size=${state.numbers.size}"
        }
    }
}

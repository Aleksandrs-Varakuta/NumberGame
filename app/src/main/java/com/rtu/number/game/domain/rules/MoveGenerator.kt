package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import javax.inject.Inject

interface MoveGenerator {
    fun generate(state: GameState): List<Move>
}

class DefaultMoveGenerator @Inject constructor() : MoveGenerator {

    override fun generate(state: GameState): List<Move> {
        if (state.numbers.size < 2) return emptyList()

        return state.numbers.indices.toList()
            .dropLast(1)
            .map { leftIndex -> Move(leftIndex = leftIndex) }
    }
}

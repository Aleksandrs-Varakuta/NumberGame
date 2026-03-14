package com.rtu.number.game.usecase

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.rules.NumberGameRules
import javax.inject.Inject

class GetAvailableMovesUseCase @Inject constructor(
    private val rules: NumberGameRules,
) {
    operator fun invoke(state: GameState): List<Move> {
        return rules.getAvailableMoves(state)
    }
}

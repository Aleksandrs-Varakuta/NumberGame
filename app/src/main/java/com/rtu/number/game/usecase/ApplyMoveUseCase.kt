package com.rtu.number.game.usecase

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.repository.GameSessionRepository
import com.rtu.number.game.domain.rules.NumberGameRules
import javax.inject.Inject

class ApplyMoveUseCase @Inject constructor(
    private val repository: GameSessionRepository,
    private val rules: NumberGameRules,
) {
    operator fun invoke(move: Move): GameState {
        val currentState = requireNotNull(repository.currentState.value) {
            "Game has not been started."
        }

        val updatedState = rules.applyMove(
            state = currentState,
            move = move,
        )

        repository.save(updatedState)
        return updatedState
    }
}

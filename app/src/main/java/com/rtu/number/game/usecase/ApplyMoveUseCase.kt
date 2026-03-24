package com.rtu.number.game.usecase

import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.repository.GameSessionRepository
import javax.inject.Inject

class ApplyMoveUseCase @Inject constructor(
    private val gameEngine: GameEngine,
    private val repository: GameSessionRepository,
) {
    operator fun invoke(
        move: Move,
    ) {
        val state = repository.currentState.value ?: return
        val newState = gameEngine.applyMove(
            state,
            move
        )
        repository.save(newState)
    }
}

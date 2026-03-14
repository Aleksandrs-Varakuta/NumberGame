package com.rtu.number.game.usecase

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.repository.GameSessionRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveGameStateUseCase @Inject constructor(
    private val repository: GameSessionRepository,
) {
    operator fun invoke(): StateFlow<GameState?> {
        return repository.currentState
    }
}

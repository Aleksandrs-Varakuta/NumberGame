package com.rtu.number.game.usecase

import com.rtu.number.game.domain.model.GameSettings
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.repository.GameSessionRepository
import com.rtu.number.game.domain.rules.NumberGameRules
import javax.inject.Inject

class StartNewGameUseCase @Inject constructor(
    private val repository: GameSessionRepository,
    private val rules: NumberGameRules,
) {
    operator fun invoke(settings: GameSettings): GameState {
        val state = rules.createInitialState(
            length = settings.cellCount,
            firstPlayer = settings.firstPlayer,
        )
        repository.save(state)
        return state
    }
}

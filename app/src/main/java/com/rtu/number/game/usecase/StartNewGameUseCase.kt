package com.rtu.number.game.usecase

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.domain.repository.GameSessionRepository
import com.rtu.number.game.domain.rules.NumberGameRules
import javax.inject.Inject

class StartNewGameUseCase @Inject constructor(
    private val repository: GameSessionRepository,
    private val rules: NumberGameRules,
) {
    operator fun invoke(
        length: Int,
        firstPlayer: PlayerId = PlayerId.FIRST,
    ): GameState {
        val state = rules.createInitialState(
            length = length,
            firstPlayer = firstPlayer,
        )
        repository.save(state)
        return state
    }
}

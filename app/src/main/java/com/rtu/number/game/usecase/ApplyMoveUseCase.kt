package com.rtu.number.game.usecase

import com.rtu.number.game.domain.ai.AIManager
import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.domain.repository.GameSessionRepository
import javax.inject.Inject

class ApplyMoveUseCase @Inject constructor(
    private val gameEngine: GameEngine,
    private val aiManager: AIManager,
    private val repository: GameSessionRepository,
) {
    operator fun invoke(
        state: GameState,
        move: Move,
        aiEnabled: Boolean,
        aiAlgorithm: String,
        aiDepth: Int,
    ): GameState {
        var newState = gameEngine.applyMove(state, move)

        if (newState.status is GameStatus.Finished || !aiEnabled) {
            repository.save(newState)
            return newState
        }

        val aiMove = aiManager.findMove(
            state = newState,
            player = PlayerId.SECOND,
            algorithm = aiAlgorithm,
            depth = aiDepth,
        )
        if (aiMove != null) {
            newState = gameEngine.applyMove(newState, aiMove)
        }

        repository.save(newState)
        return newState
    }
}

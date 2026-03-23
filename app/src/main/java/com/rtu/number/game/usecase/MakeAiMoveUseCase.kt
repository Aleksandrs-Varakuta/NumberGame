package com.rtu.number.game.usecase

import com.rtu.number.game.domain.ai.AIManager
import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.domain.repository.GameSessionRepository
import javax.inject.Inject

class MakeAiMoveUseCase @Inject constructor(
    private val gameEngine: GameEngine,
    private val aiManager: AIManager,
    private val repository: GameSessionRepository,
) {
    suspend operator fun invoke(
        aiAlgorithm: AiAlgorithm,
        aiDepth: Int,
    ): Move? {
        val state = repository.currentState.value ?: return null

        val aiMove = aiManager.findMove(
            state = state,
            player = PlayerId.SECOND,
            algorithm = aiAlgorithm,
            depth = aiDepth,
        )
        val newState = aiMove?.let {
            gameEngine.applyMove(
                state,
                it
            )
        } ?: state

        repository.save(newState)
        return aiMove
    }
}

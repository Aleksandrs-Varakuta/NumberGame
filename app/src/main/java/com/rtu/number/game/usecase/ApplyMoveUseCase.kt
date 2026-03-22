package com.rtu.number.game.domain.usecase

import com.rtu.number.game.domain.ai.AIManager
import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject

class ApplyMoveUseCase @Inject constructor(
    private val gameEngine: GameEngine,
    private val aiManager: AIManager
) {

    operator fun invoke(
        state: GameState,
        move: com.rtu.number.game.domain.model.Move,
        aiEnabled: Boolean,
        aiAlgorithm: String
    ): GameState {

        // ход игрока
        var newState = gameEngine.applyMove(state, move)

        // если игра закончена — AI не ходит
        if (newState.status.isFinished())
            return newState

        // если AI выключен — возвращаем состояние
        if (!aiEnabled)
            return newState

        // ход AI
        val aiMove = aiManager.findMove(
            newState,
            PlayerId.SECOND,
            aiAlgorithm
        )

        if (aiMove != null) {
            newState = gameEngine.applyMove(newState, aiMove)
        }

        return newState
    }
}
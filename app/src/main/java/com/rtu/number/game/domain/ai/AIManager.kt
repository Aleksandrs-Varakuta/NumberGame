package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIManager @Inject constructor(
    private val engine: GameEngine,
    private val evaluator: Evaluator,
) {
    fun findMove(
        state: GameState,
        player: PlayerId,
        algorithm: String,
        depth: Int,
    ): Move? {
        return when (algorithm) {
            "alphabeta" -> AlphaBetaAlgorithm(evaluator, depth, engine).findBestMove(state, player)
            "minimax"   -> MiniMaxAlgorithm(evaluator, depth, engine).findBestMove(state, player)
            else        -> MiniMaxAlgorithm(evaluator, depth, engine).findBestMove(state, player)
        }
    }
}

package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIManager @Inject constructor(
    private val engine: GameEngine
) {

    private val evaluator: Evaluator = SimpleEvaluator()

    private val minimax by lazy {
        MiniMaxAlgorithm(
            evaluator = evaluator,
            maxDepth = 5,
            engine = engine
        )
    }

    private val alphabeta by lazy {
        AlphaBetaAlgorithm(
            evaluator = evaluator,
            maxDepth = 7,
            engine = engine
        )
    }

    fun findMove(
        state: GameState,
        player: PlayerId,
        algorithm: String
    ): Move? {

        return when (algorithm) {

            "alphabeta" ->
                alphabeta.findBestMove(state, player)

            "minimax" ->
                minimax.findBestMove(state, player)

            else ->
                minimax.findBestMove(state, player)
        }
    }
}
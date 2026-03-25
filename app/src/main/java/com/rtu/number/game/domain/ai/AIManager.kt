package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.GameStatisticsRepository
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class AIManager @Inject constructor(
    private val engine: GameEngine,
    private val evaluator: Evaluator,
    private val gameStatistics: GameStatisticsRepository
) {
    suspend fun findMove(
        state: GameState,
        player: PlayerId,
        algorithm: AiAlgorithm,
        depth: Int,
    ): Move? {
        val algorithm = if (algorithm == AiAlgorithm.ALPHA_BETA) {
            AlphaBetaAlgorithm(
                evaluator,
                depth,
                engine
            )
        } else {
            MiniMaxAlgorithm(
                evaluator,
                depth,
                engine
            )
        }
        var move: Move? = null
        val thinkingTime = measureTimeMillis {
            move = algorithm.findBestMove(
                state,
                player
            )
        }
        val nodesEvaluated = algorithm.getEvaluatedNodesCount()
        gameStatistics.addStatistics(
            thinkingTime,
            nodesEvaluated
        )
        return move
    }
}

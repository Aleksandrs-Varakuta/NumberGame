package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class MiniMaxAlgorithm(
    evaluator: Evaluator,
    maxDepth: Int,
    engine: GameEngine
) : GameAlgorithm(evaluator, maxDepth, engine) {

    override fun findBestMove(state: GameState, player: PlayerId): Move? {

        resetCounters()

        val root = GameNode(state)

        val bestValue = minimax(root, maxDepth, true, player)

        return root.children.firstOrNull {
            abs(it.evaluation - bestValue) < 0.0001
        }?.move
    }

    private fun minimax(
        node: GameNode,
        depth: Int,
        isMaximizing: Boolean,
        player: PlayerId
    ): Double {

        if (depth == 0 || node.isTerminal()) {

            val eval = evaluator.evaluate(node.state, player)

            node.evaluation = eval

            nodesEvaluated++

            return eval
        }

        generateChildren(node, node.depth)

        if (isMaximizing) {

            var maxEval = Double.NEGATIVE_INFINITY

            for (child in node.children) {

                val eval = minimax(child, depth - 1, false, player)

                maxEval = max(maxEval, eval)
            }

            node.evaluation = maxEval

            return maxEval
        } else {

            var minEval = Double.POSITIVE_INFINITY

            for (child in node.children) {

                val eval = minimax(child, depth - 1, true, player)

                minEval = min(minEval, eval)
            }

            node.evaluation = minEval

            return minEval
        }
    }

    override fun getName() = "Minimax"
}
package com.rtu.number.game.logic.algorithms

import com.rtu.number.game.logic.coreLogic.*
import com.rtu.number.game.logic.evaluation.Evaluator
import kotlin.math.max
import kotlin.math.min

class MiniMaxAlgorithm(evaluator: Evaluator, maxDepth: Int) : GameAlgorithm(evaluator, maxDepth) {

    override fun findBestMove(state: GameState, player: Int): Move? {
        resetCounters()
        val root = GameNode(state)
        val bestValue = minimax(root, maxDepth, true, player)

        return root.children.firstOrNull {
            kotlin.math.abs(it.evaluation - bestValue) < 0.0001
        }?.move
    }

    private fun minimax(node: GameNode, depth: Int, isMaximizing: Boolean, player: Int): Double {
        if (depth == 0 || node.isTerminal()) {
            val eval = evaluator.evaluate(node.state, player)
            node.evaluation = eval
            nodesEvaluated++
            return eval
        }

        generateChildren(node, node.depth)

        if (node.children.isEmpty()) {
            val eval = evaluator.evaluate(node.state, player)
            node.evaluation = eval
            nodesEvaluated++
            return eval
        }

        return if (isMaximizing) {
            var maxEval = Double.NEGATIVE_INFINITY
            for (child in node.children) {
                val eval = minimax(child, depth - 1, false, player)
                maxEval = max(maxEval, eval)
            }
            node.evaluation = maxEval
            maxEval
        } else {
            var minEval = Double.POSITIVE_INFINITY
            for (child in node.children) {
                val eval = minimax(child, depth - 1, true, player)
                minEval = min(minEval, eval)
            }
            node.evaluation = minEval
            minEval
        }
    }

    override fun getName() = "Minimax"
}

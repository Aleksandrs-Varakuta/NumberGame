package com.rtu.number.game.logic.algorithms

import com.rtu.number.game.logic.coreLogic.*
import com.rtu.number.game.logic.evaluation.Evaluator

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class AlphaBetaAlgorithm(evaluator: Evaluator, maxDepth: Int) : GameAlgorithm(evaluator, maxDepth) {

    override fun findBestMove(state: GameState, player: Int): Move? {
        resetCounters()
        val root = GameNode(state)
        val bestValue = alphaBeta(root, maxDepth,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            true, player)

        return root.children.firstOrNull {
            abs(it.evaluation - bestValue) < 0.0001
        }?.move
    }

    private fun alphaBeta(node: GameNode, depth: Int, alpha: Double, beta: Double,
                          isMaximizing: Boolean, player: Int): Double {
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

        var currentAlpha = alpha
        var currentBeta = beta

        return if (isMaximizing) {
            var maxEval = Double.NEGATIVE_INFINITY
            for (child in node.children) {
                val eval = alphaBeta(child, depth - 1, currentAlpha, currentBeta, false, player)
                maxEval = max(maxEval, eval)
                currentAlpha = max(currentAlpha, eval)
                if (currentBeta <= currentAlpha) break // Beta cutoff
            }
            node.evaluation = maxEval
            maxEval
        } else {
            var minEval = Double.POSITIVE_INFINITY
            for (child in node.children) {
                val eval = alphaBeta(child, depth - 1, currentAlpha, currentBeta, true, player)
                minEval = min(minEval, eval)
                currentBeta = min(currentBeta, eval)
                if (currentBeta <= currentAlpha) break // Alpha cutoff
            }
            node.evaluation = minEval
            minEval
        }
    }

    override fun getName() = "Alpha-Beta"
}

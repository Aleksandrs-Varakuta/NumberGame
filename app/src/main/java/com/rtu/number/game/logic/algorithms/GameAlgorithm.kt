package com.rtu.number.game.logic.algorithms

import com.rtu.number.game.logic.coreLogic.*
import com.rtu.number.game.logic.evaluation.Evaluator

abstract class GameAlgorithm(
    protected val evaluator: Evaluator,
    var maxDepth: Int
) {
    var nodesGenerated: Long = 0
        protected set
    var nodesEvaluated: Long = 0
        protected set

    abstract fun findBestMove(state: GameState, player: Int): Move?

    protected fun generateChildren(node: GameNode, currentDepth: Int) {
        if (currentDepth >= maxDepth || node.isTerminal() || node.isExpanded) return

        val validMoves = node.state.getValidMoves()
        for (move in validMoves) {
            val newState = node.state.clone()
            newState.makeMove(move)

            val child = GameNode(newState, node, move, currentDepth + 1)
            node.addChild(child)
            nodesGenerated++
        }

        node.isExpanded = true
    }

    fun resetCounters() {
        nodesGenerated = 0
        nodesEvaluated = 0
    }

    open fun getName() = this::class.simpleName ?: "Algorithm"
}

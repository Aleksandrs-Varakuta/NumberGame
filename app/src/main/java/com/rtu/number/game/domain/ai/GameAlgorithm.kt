package com.rtu.number.game.domain.ai

import com.rtu.number.game.domain.engine.GameEngine
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId

abstract class GameAlgorithm(
    protected val evaluator: Evaluator,
    protected val maxDepth: Int,
    protected val engine: GameEngine
) {

    protected var nodesEvaluated = 0

    protected fun resetCounters() {
        nodesEvaluated = 0
    }

    abstract suspend fun findBestMove(
        state: GameState,
        player: PlayerId
    ): Move?

    abstract fun getName(): String

    protected fun generateChildren(node: GameNode, depth: Int) {

        val moves = engine.getAvailableMoves(node.state)

        for (move in moves) {

            val newState = engine.applyMove(node.state, move)

            val child = GameNode(
                state = newState,
                move = move,
                depth = depth + 1
            )

            node.children.add(child)
        }
    }
}
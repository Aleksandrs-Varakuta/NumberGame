package com.rtu.number.game.domain

import com.rtu.number.game.model.GameNode
import com.rtu.number.game.model.GameNumber
import com.rtu.number.game.model.Player
import kotlinx.coroutines.flow.StateFlow

interface GameGraphRepository {

    val headerNode: StateFlow<GameNode>
    val currentNode: StateFlow<GameNode>

    fun generateGameGraph(
        players: List<Player>,
        maxDepth: Int
    )

    fun makeMove(chosenNumbers: Pair<GameNumber, GameNumber>)
}
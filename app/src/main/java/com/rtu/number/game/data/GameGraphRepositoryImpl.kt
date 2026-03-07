package com.rtu.number.game.data

import com.rtu.number.game.domain.GameGraphRepository
import com.rtu.number.game.model.GameMoves
import com.rtu.number.game.model.GameNode
import com.rtu.number.game.model.GameNumber
import com.rtu.number.game.model.Player
import com.rtu.number.game.model.getNextPlayersUUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameGraphRepositoryImpl : GameGraphRepository {

    private val _headerNode = MutableStateFlow(GameNode())

    override val headerNode: StateFlow<GameNode> = _headerNode

    private val _currentNode = MutableStateFlow(GameNode())

    override val currentNode: StateFlow<GameNode> = _currentNode

    override fun generateGameGraph(
        players: List<Player>,
        maxDepth: Int
    ) {

        val rootNode = GameNode(
            splitNumber = generateSplitNumber(),
            players = players,
            currentPlayersUUID = players.firstOrNull()?.id?:""
        )
        val gameNodes = mutableListOf(rootNode)
        val existingNodes = mutableSetOf(rootNode)

        var depthLevel = 0
        var nodesLeftOnCurrentLevel = 1
        var nodesOnNextLevel = 0

        while (gameNodes.isNotEmpty() && depthLevel < maxDepth) {
            val currentNode = gameNodes.firstOrNull()
            if (currentNode == null) break
            gameNodes.drop(1)

            val allChildrenNodes = getChildrenNodes(currentNode)
            val uniqueChildrenNodes = allChildrenNodes.map { node ->
                val existingNode = existingNodes.firstOrNull { it == node }
                if (existingNode == null) {
                    existingNodes.add(node)
                    node
                } else {
                    existingNode
                }
            }

            currentNode.children.addAll(uniqueChildrenNodes)

            val innerNodes = uniqueChildrenNodes.filter { node -> node.splitNumber.size > 1 }
            gameNodes.addAll(innerNodes)
            nodesOnNextLevel += innerNodes.size

            nodesLeftOnCurrentLevel--

            if (nodesLeftOnCurrentLevel == 0) {
                depthLevel++
                nodesLeftOnCurrentLevel = nodesOnNextLevel
                nodesOnNextLevel = 0
            }
        }

        _headerNode.value = rootNode
        _currentNode.value = rootNode
    }

    override fun makeMove(
        chosenNumbers: Pair<GameNumber, GameNumber>,
    ) {
        val move = getMove(chosenNumbers)
        val newSplitNumber = getSplitNumberAfterMove(
            chosenNumbers,
            move,
            currentNode.value
        )
        val playersWithNewScores = getPlayersWithNewScores(
            move,
            currentNode.value
        )

        _currentNode.update {
            it.copy(
                splitNumber = newSplitNumber,
                players = playersWithNewScores,
                currentPlayersUUID = currentNode.value.getNextPlayersUUID()
            )
        }
    }

    /*
    * TODO Create API to build displayable graph on server and then return here as List<List<GameNode>>
    * Maybe better to extract all logic with graph building on server, because on phone it's impossible to generate large graphs
    * */
    fun getDisplayableGraph(startNode: GameNode): List<List<GameNode>> {
        val graphNodes = mutableListOf<List<GameNode>>()
        val pendingNodes = mutableListOf(startNode)

        var nodesLeftOnCurrentLevel = 1
        var nodesOnNextLevel = 0
        val nodesOnCurrentLevel = mutableListOf<GameNode>()

        while (pendingNodes.isNotEmpty()) {
            val currentNode = pendingNodes.removeAt(0)
            nodesOnCurrentLevel.add(currentNode)

            pendingNodes.addAll(currentNode.children)
            nodesOnNextLevel += currentNode.children.size

            nodesLeftOnCurrentLevel--

            if (nodesLeftOnCurrentLevel == 0) {
                graphNodes.add(nodesOnCurrentLevel.toList())
                nodesOnCurrentLevel.clear()

                nodesLeftOnCurrentLevel = nodesOnNextLevel
                nodesOnNextLevel = 0
            }
        }

        return graphNodes
    }

    private fun generateSplitNumber(): List<GameNumber> {
        val numberVariants = (1 .. 9).toList()
        val numberLength = Random.nextInt(
            5,
            10
        )
        val splitNumber = mutableListOf<GameNumber>()
        for (i in 0 until numberLength) {
            splitNumber.add(
                GameNumber(
                    number = numberVariants.random(),
                    index = i
                )
            )
        }
        return splitNumber
    }

    private fun getChildrenNodes(gameNode: GameNode): List<GameNode> {
        val childrenNodes = mutableListOf<GameNode>()
        for (i in 0 until gameNode.splitNumber.size - 1) {
            val chosenNumbers = Pair(
                gameNode.splitNumber[i],
                gameNode.splitNumber[i + 1]
            )
            val move = getMove(chosenNumbers)
            val newSplitNumber = getSplitNumberAfterMove(
                chosenNumbers,
                move,
                gameNode
            )
            val playersWithNewScores = getPlayersWithNewScores(
                move,
                gameNode
            )
            childrenNodes.add(
                GameNode(
                    splitNumber = newSplitNumber,
                    players = playersWithNewScores,
                    currentPlayersUUID = gameNode.getNextPlayersUUID()
                )
            )

        }
        return childrenNodes
    }

    private fun getMove(
        chosenNumbers: Pair<GameNumber, GameNumber>
    ): GameMoves {
        val sum = chosenNumbers.first.number + chosenNumbers.second.number
        return when {
            sum > LIMIT_SUM -> GameMoves.Larger
            sum < LIMIT_SUM -> GameMoves.Smaller
            else -> GameMoves.Equals
        }
    }

    private fun getSplitNumberAfterMove(
        chosenNumbers: Pair<GameNumber, GameNumber>,
        move: GameMoves,
        gameNode: GameNode,
    ): List<GameNumber> {
        val currentSplitNumber = gameNode.splitNumber
        val newSplitNumber = mutableListOf<GameNumber>()
        val sortedChosenNumbers = if (chosenNumbers.first.index < chosenNumbers.second.index) chosenNumbers else Pair(
            chosenNumbers.second,
            chosenNumbers.first
        )

        for (numberIndex in 0 .. currentSplitNumber.size) {
            when (numberIndex) {
                sortedChosenNumbers.first.index -> newSplitNumber.add(
                    GameNumber(
                        move.newNumber,
                        numberIndex
                    )
                )

                sortedChosenNumbers.second.index -> continue
                else -> {
                    val currentNumber = currentSplitNumber.elementAtOrNull(numberIndex)
                    if (currentNumber == null) continue

                    if (numberIndex > sortedChosenNumbers.second.index) {
                        newSplitNumber.add(currentNumber.copy(index = numberIndex - 1))
                    } else {
                        newSplitNumber.add(currentNumber)
                    }

                }
            }
        }
        return newSplitNumber
    }

    private fun getPlayersWithNewScores(
        move: GameMoves,
        gameNode: GameNode
    ): List<Player> {
        val playerStates = gameNode.players
        val currentPlayersUUID = gameNode.currentPlayersUUID

        return when (move) {
            GameMoves.Larger -> playerStates.map { player -> if (currentPlayersUUID == player.id) player.copy(score = player.score + 1) else player }
            GameMoves.Equals -> playerStates.map { player -> player.copy(score = player.score + 1) }
            GameMoves.Smaller -> playerStates.map { player -> if (currentPlayersUUID != player.id) player.copy(score = player.score - 1) else player }
        }

    }

    companion object {
        const val LIMIT_SUM = 7

    }
}
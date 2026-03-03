package com.rtu.number.game.logic.coreLogic

class GameNode(
    val state: GameState,
    val parent: GameNode? = null,
    val move: Move? = null,
    val depth: Int = 0
) {
    val children = mutableListOf<GameNode>()
    var evaluation: Double = 0.0
    var isExpanded: Boolean = false

    fun addChild(child: GameNode) {
        children.add(child)
    }

    fun isLeaf() = children.isEmpty()
    fun isTerminal() = state.isGameOver()
}

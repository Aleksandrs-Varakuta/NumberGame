package com.rtu.number.game.logic.coreLogic

abstract class GameState : Cloneable {
    var currentPlayer: Int = 1
    var status: GameStatus = GameStatus.IN_PROGRESS

    abstract fun isGameOver(): Boolean
    abstract fun isValidMove(move: Move): Boolean
    abstract fun getValidMoves(): List<Move>
    abstract fun makeMove(move: Move)
    abstract fun undoMove(move: Move)
    abstract fun switchPlayer()
    abstract fun getWinner(): Int
    abstract fun getBoardString(): String

    public abstract override fun clone(): GameState
}

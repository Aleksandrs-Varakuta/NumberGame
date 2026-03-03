package com.rtu.number.game.logic.coreLogic

enum class GameStatus {
    IN_PROGRESS,
    PLAYER1_WIN,
    PLAYER2_WIN,
    DRAW;

    fun isGameOver() = this != IN_PROGRESS
}

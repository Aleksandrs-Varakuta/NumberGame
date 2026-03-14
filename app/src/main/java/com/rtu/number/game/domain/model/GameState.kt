package com.rtu.number.game.domain.model

data class GameState(
    val numbers: List<Int>,
    val firstPlayerScore: Int = 0,
    val secondPlayerScore: Int = 0,
    val currentPlayer: PlayerId = PlayerId.FIRST,
    val moveNumber: Int = 0,
) {
    init {
        require(numbers.isNotEmpty()) {
            "Numbers must not be empty."
        }
        require(numbers.all { it in 1..9 }) {
            "All numbers must be in range 1..9."
        }
    }
}

package com.rtu.number.game.model

enum class GameMoves {
    Larger,
    Equals,
    Smaller;

    val newNumber: Int
        get() = when (this) {
            Larger -> 1
            Equals -> 2
            Smaller -> 3
        }

}
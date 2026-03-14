package com.rtu.number.game.domain.model

data class Move(
    val leftIndex: Int,
) {
    val rightIndex: Int
        get() = leftIndex + 1
}

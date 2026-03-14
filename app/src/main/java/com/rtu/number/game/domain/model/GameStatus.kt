package com.rtu.number.game.domain.model

sealed interface GameStatus {
    data object InProgress : GameStatus

    data class Finished(
        val winner: PlayerId?,
    ) : GameStatus
}

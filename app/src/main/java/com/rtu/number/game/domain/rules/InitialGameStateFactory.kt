package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject
import kotlin.random.Random

interface InitialGameStateFactory {
    fun create(
        length: Int,
        firstPlayer: PlayerId = PlayerId.FIRST,
    ): GameState
}

class RandomInitialGameStateFactory @Inject constructor() : InitialGameStateFactory {

    override fun create(
        length: Int,
        firstPlayer: PlayerId,
    ): GameState {
        require(length in 15..25) {
            "Initial length must be in range 15..25."
        }

        val numbers = List(length) {
            Random.nextInt(from = 1, until = 10)
        }

        return GameState(
            numbers = numbers,
            currentPlayer = firstPlayer,
        )
    }
}

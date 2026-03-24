package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.MAX_CELL_COUNT
import com.rtu.number.game.domain.model.MIN_CELL_COUNT
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
        require(length in MIN_CELL_COUNT..MAX_CELL_COUNT) {
            "Initial length must be in range $MIN_CELL_COUNT..$MAX_CELL_COUNT."
        }

        val numbers = List(length) {
            Random.nextInt(
                from = 1,
                until = 10
            )
        }

        return GameState(
            numbers = numbers,
            currentPlayer = firstPlayer,
        )
    }
}

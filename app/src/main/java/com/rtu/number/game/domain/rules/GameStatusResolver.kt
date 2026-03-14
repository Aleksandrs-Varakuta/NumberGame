package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject

interface GameStatusResolver {
    fun resolve(state: GameState): GameStatus
}

class DefaultGameStatusResolver @Inject constructor() : GameStatusResolver {

    override fun resolve(state: GameState): GameStatus {
        if (state.numbers.size >= 2) {
            return GameStatus.InProgress
        }

        return when {
            state.firstPlayerScore > state.secondPlayerScore -> {
                GameStatus.Finished(winner = PlayerId.FIRST)
            }

            state.secondPlayerScore > state.firstPlayerScore -> {
                GameStatus.Finished(winner = PlayerId.SECOND)
            }

            else -> {
                GameStatus.Finished(winner = null)
            }
        }
    }
}

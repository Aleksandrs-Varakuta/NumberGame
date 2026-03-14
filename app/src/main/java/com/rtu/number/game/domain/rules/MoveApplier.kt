package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.opponent
import javax.inject.Inject

interface MoveApplier {
    fun apply(
        state: GameState,
        move: Move,
    ): GameState
}

class DefaultMoveApplier @Inject constructor(
    private val moveValidator: MoveValidator,
    private val moveResolver: MoveResolver,
) : MoveApplier {

    override fun apply(
        state: GameState,
        move: Move,
    ): GameState {
        moveValidator.requireValid(state, move)

        val leftValue = state.numbers[move.leftIndex]
        val rightValue = state.numbers[move.rightIndex]

        val moveResolution = moveResolver.resolve(
            leftValue = leftValue,
            rightValue = rightValue,
            currentPlayer = state.currentPlayer,
        )

        val updatedNumbers = buildList {
            addAll(state.numbers.subList(0, move.leftIndex))
            add(moveResolution.replacementValue)
            addAll(state.numbers.subList(move.rightIndex + 1, state.numbers.size))
        }

        return GameState(
            numbers = updatedNumbers,
            firstPlayerScore = state.firstPlayerScore + moveResolution.scoreChange.firstPlayerDelta,
            secondPlayerScore = state.secondPlayerScore + moveResolution.scoreChange.secondPlayerDelta,
            currentPlayer = state.currentPlayer.opponent(),
            moveNumber = state.moveNumber + 1,
        )
    }
}

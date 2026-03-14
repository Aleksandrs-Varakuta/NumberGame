package com.rtu.number.game.domain.rules

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NumberGameRules @Inject constructor(
    private val initialGameStateFactory: InitialGameStateFactory,
    private val moveGenerator: MoveGenerator,
    private val moveValidator: MoveValidator,
    private val moveApplier: MoveApplier,
    private val gameStatusResolver: GameStatusResolver,
) {
    fun createInitialState(
        length: Int,
        firstPlayer: PlayerId = PlayerId.FIRST,
    ): GameState {
        return initialGameStateFactory.create(
            length = length,
            firstPlayer = firstPlayer,
        )
    }

    fun getAvailableMoves(state: GameState): List<Move> {
        return moveGenerator.generate(state)
    }

    fun isMoveValid(
        state: GameState,
        move: Move,
    ): Boolean {
        return moveValidator.isValid(state, move)
    }

    fun applyMove(
        state: GameState,
        move: Move,
    ): GameState {
        return moveApplier.apply(state, move)
    }

    fun getStatus(state: GameState): GameStatus {
        return gameStatusResolver.resolve(state)
    }
}

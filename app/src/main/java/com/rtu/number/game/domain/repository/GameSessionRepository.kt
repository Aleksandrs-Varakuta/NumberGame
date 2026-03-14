package com.rtu.number.game.domain.repository

import com.rtu.number.game.domain.model.GameState
import kotlinx.coroutines.flow.StateFlow

interface GameSessionRepository {
    val currentState: StateFlow<GameState?>

    fun save(state: GameState)

    fun clear()
}

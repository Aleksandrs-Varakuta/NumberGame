package com.rtu.number.game.data.repository

import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.repository.GameSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryGameSessionRepository @Inject constructor() : GameSessionRepository {

    private val mutableCurrentState = MutableStateFlow<GameState?>(null)

    override val currentState: StateFlow<GameState?> = mutableCurrentState.asStateFlow()

    override fun save(state: GameState) {
        mutableCurrentState.value = state
    }

    override fun clear() {
        mutableCurrentState.value = null
    }
}

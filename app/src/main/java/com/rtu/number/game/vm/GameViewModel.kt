package com.rtu.number.game.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.usecase.ApplyMoveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val applyMoveUseCase: ApplyMoveUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState.initial())
    val state: StateFlow<GameState> = _state

    private var aiEnabled = true
    private var aiAlgorithm = "alphabeta"

    fun makeMove(move: Move) {

        viewModelScope.launch {

            val newState = applyMoveUseCase(
                state = _state.value,
                move = move,
                aiEnabled = aiEnabled,
                aiAlgorithm = aiAlgorithm
            )

            _state.value = newState
        }
    }
}
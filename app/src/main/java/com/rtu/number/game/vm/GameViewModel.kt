package com.rtu.number.game.vm

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.domain.rules.NumberGameRules
import com.rtu.number.game.usecase.ApplyMoveUseCase
import com.rtu.number.game.usecase.GetAvailableMovesUseCase
import com.rtu.number.game.usecase.ObserveGameStateUseCase
import com.rtu.number.game.usecase.StartNewGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

@HiltViewModel
class GameViewModel @Inject constructor(
    private val startNewGameUseCase: StartNewGameUseCase,
    private val observeGameStateUseCase: ObserveGameStateUseCase,
    private val getAvailableMovesUseCase: GetAvailableMovesUseCase,
    private val applyMoveUseCase: ApplyMoveUseCase,
    private val rules: NumberGameRules,
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(UiState())
    val uiState = mutableUiState.asStateFlow()

    init {
        observeGameState()
        startNewGame()
    }

    fun startNewGame(
        length: Int = DEFAULT_INITIAL_LENGTH,
        firstPlayer: PlayerId = PlayerId.FIRST,
    ) {
        runCatching {
            startNewGameUseCase(
                length = length,
                firstPlayer = firstPlayer,
            )
        }.onFailure { throwable ->
            mutableUiState.update {
                it.copy(errorMessage = throwable.message)
            }
        }
    }

    fun onRestart() {
        startNewGame()
    }

    fun onNumberClick(index: Int) {
        val firstSelectedIndex = uiState.value.firstSelectedIndex

        if (firstSelectedIndex == null) {
            mutableUiState.update {
                it.copy(firstSelectedIndex = index)
            }
            return
        }

        if (firstSelectedIndex == index) {
            mutableUiState.update {
                it.copy(firstSelectedIndex = null)
            }
            return
        }

        val isNeighbour = abs(firstSelectedIndex - index) == 1
        if (!isNeighbour) {
            mutableUiState.update {
                it.copy(firstSelectedIndex = index)
            }
            return
        }

        val leftIndex = minOf(firstSelectedIndex, index)
        applyMove(Move(leftIndex = leftIndex))
    }

    private fun applyMove(move: Move) {
        runCatching {
            applyMoveUseCase(move)
        }.onSuccess {
            mutableUiState.update { currentState ->
                currentState.copy(
                    firstSelectedIndex = null,
                    errorMessage = null,
                )
            }
        }.onFailure { throwable ->
            mutableUiState.update {
                it.copy(
                    firstSelectedIndex = null,
                    errorMessage = throwable.message,
                )
            }
        }
    }

    private fun observeGameState() {
        viewModelScope.launch {
            observeGameStateUseCase().collectLatest { state ->
                if (state == null) {
                    mutableUiState.value = UiState()
                    return@collectLatest
                }

                mutableUiState.value = UiState(
                    numbers = state.numbers,
                    firstPlayerScore = state.firstPlayerScore,
                    secondPlayerScore = state.secondPlayerScore,
                    currentPlayer = state.currentPlayer,
                    availableMoves = getAvailableMovesUseCase(state),
                    status = rules.getStatus(state),
                    isGameStarted = true,
                    firstSelectedIndex = mutableUiState.value.firstSelectedIndex,
                    errorMessage = null,
                )
            }
        }
    }

    @Stable
    data class UiState(
        val numbers: List<Int> = emptyList(),
        val firstPlayerScore: Int = 0,
        val secondPlayerScore: Int = 0,
        val currentPlayer: PlayerId = PlayerId.FIRST,
        val availableMoves: List<Move> = emptyList(),
        val status: GameStatus = GameStatus.InProgress,
        val firstSelectedIndex: Int? = null,
        val isGameStarted: Boolean = false,
        val errorMessage: String? = null,
    )

    companion object {
        private const val DEFAULT_INITIAL_LENGTH = 15
    }
}

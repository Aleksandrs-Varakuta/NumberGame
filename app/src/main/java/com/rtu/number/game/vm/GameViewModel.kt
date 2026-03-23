package com.rtu.number.game.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.GameMode
import com.rtu.number.game.domain.model.GameSettings
import com.rtu.number.game.domain.model.GameState
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.usecase.ApplyMoveUseCase
import com.rtu.number.game.usecase.MakeAiMoveUseCase
import com.rtu.number.game.usecase.ObserveGameStateUseCase
import com.rtu.number.game.usecase.StartNewGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class GameViewModel @Inject constructor(
    private val startNewGameUseCase: StartNewGameUseCase,
    private val applyMoveUseCase: ApplyMoveUseCase,
    private val makeAiMoveUseCase: MakeAiMoveUseCase,
    private val observeGameStateUseCase: ObserveGameStateUseCase,
) : ViewModel() {

    data class UiState(
        val numbers: List<Int> = emptyList(),
        val firstPlayerScore: Int = 0,
        val secondPlayerScore: Int = 0,
        val currentPlayer: PlayerId = PlayerId.FIRST,
        val status: GameStatus = GameStatus.InProgress,
        val firstSelectedIndex: Int? = null,
        val settings: GameSettings = GameSettings(),
        val moveToAnimate: Move? = null,
    ) {
        val player1Name: String get() = settings.player1Name
        val player2Name: String get() = settings.player2Name

        val isAiTurn: Boolean get() = currentPlayer == PlayerId.SECOND && settings.gameMode == GameMode.HUMAN_VS_AI

        val canInteract: Boolean get() = status is GameStatus.InProgress && !isAiTurn && moveToAnimate == null
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var aiThinkJob: Job? = null


    init {
        uiState.map { it.moveToAnimate }
            .distinctUntilChanged()
            .onEach { move ->
                val state = observeGameStateUseCase().value ?: return@onEach
                if (move == null) {
                    _uiState.update {
                        it.copy(
                            numbers = state.numbers,
                            firstPlayerScore = state.firstPlayerScore,
                            secondPlayerScore = state.secondPlayerScore,
                            currentPlayer = state.currentPlayer,
                            status = state.status,
                            firstSelectedIndex = null,
                        )
                    }
                    val settings = _uiState.value.settings
                    if (_uiState.value.isAiTurn) {
                        makeAiMove(
                            state,
                            settings
                        )
                    }
                }

            }
            .launchIn(viewModelScope)

    }

    private fun makeAiMove(
        state: GameState,
        settings: GameSettings
    ) {
        aiThinkJob?.cancel()

        if (state.status == GameStatus.InProgress) {
            aiThinkJob = viewModelScope.launch {
                val aiMove = makeAiMoveUseCase(
                    aiAlgorithm = settings.aiAlgorithm,
                    aiDepth = settings.aiDepth,
                )
                _uiState.update {
                    it.copy(
                        moveToAnimate = aiMove,
                    )
                }

            }
        }
    }

    fun onRestart() {
        val state = startNewGameUseCase(_uiState.value.settings)
        _uiState.update {
            it.copy(
                numbers = state.numbers,
                firstPlayerScore = state.firstPlayerScore,
                secondPlayerScore = state.secondPlayerScore,
                currentPlayer = state.currentPlayer,
                status = state.status,
                firstSelectedIndex = null,
            )
        }
        aiThinkJob?.cancel()
        aiThinkJob = null
    }

    fun onNumberClick(index: Int) {
        val selected = _uiState.value.firstSelectedIndex
        when {
            selected == index -> _uiState.update {
                it.copy(
                    firstSelectedIndex = null
                )
            }

            selected == null -> _uiState.update {
                it.copy(
                    firstSelectedIndex = index
                )
            }

            abs(selected - index) == 1 -> {
                val move = Move(
                    leftIndex = minOf(
                        selected,
                        index
                    )
                )
                _uiState.update {
                    it.copy(
                        firstSelectedIndex = null,
                    )
                }
                applyMoveUseCase(move = move)
                _uiState.update {
                    it.copy(
                        moveToAnimate = move
                    )
                }
            }

            else -> _uiState.update {
                it.copy(firstSelectedIndex = index)
            }
        }
    }

    fun onChangeCellCount(newCellCount: Int) =
        _uiState.update { it.copy(settings = it.settings.copy(cellCount = newCellCount)) }

    fun onChangeGameMode(newGameMode: GameMode) =
        _uiState.update { it.copy(settings = it.settings.copy(gameMode = newGameMode)) }

    fun onChangePlayer1Name(newPlayer1Name: String) =
        _uiState.update { it.copy(settings = it.settings.copy(player1Name = newPlayer1Name)) }

    fun onChangePlayer2Name(newPlayer2Name: String) =
        _uiState.update { it.copy(settings = it.settings.copy(player2Name = newPlayer2Name)) }

    fun onChangeFirstPlayer(newFirstPlayer: PlayerId) =
        _uiState.update { it.copy(settings = it.settings.copy(firstPlayer = newFirstPlayer)) }

    fun onChangeAlgorithm(newAiAlgorithm: AiAlgorithm) =
        _uiState.update { it.copy(settings = it.settings.copy(aiAlgorithm = newAiAlgorithm)) }

    fun onChangeAiDepth(newAiDepth: Int) =
        _uiState.update { it.copy(settings = it.settings.copy(aiDepth = newAiDepth)) }

    fun onMoveAnimationFinished() = _uiState.update {
        it.copy(moveToAnimate = null)
    }

}

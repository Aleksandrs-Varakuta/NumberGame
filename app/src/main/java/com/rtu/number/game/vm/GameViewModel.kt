package com.rtu.number.game.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.GameMode
import com.rtu.number.game.domain.model.GameSettings
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.Move
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.usecase.ApplyMoveUseCase
import com.rtu.number.game.usecase.ObserveGameStateUseCase
import com.rtu.number.game.usecase.StartNewGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val startNewGameUseCase: StartNewGameUseCase,
    private val applyMoveUseCase: ApplyMoveUseCase,
    private val observeGameStateUseCase: ObserveGameStateUseCase,
) : ViewModel() {

    data class UiState(
        val numbers: List<Int> = emptyList(),
        val firstPlayerScore: Int = 0,
        val secondPlayerScore: Int = 0,
        val currentPlayer: PlayerId = PlayerId.FIRST,
        val status: GameStatus = GameStatus.InProgress,
        val firstSelectedIndex: Int? = null,
        val errorMessage: String? = null,
        val settings: GameSettings = GameSettings(),
        val isSettingsOpen: Boolean = false,
        val draftSettings: GameSettings = GameSettings(),
    ) {
        val player1Name: String get() = settings.player1Name
        val player2Name: String get() = settings.player2Name
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeGameStateUseCase()
            .onEach { state ->
                if (state != null) {
                    _uiState.update {
                        it.copy(
                            numbers = state.numbers,
                            firstPlayerScore = state.firstPlayerScore,
                            secondPlayerScore = state.secondPlayerScore,
                            currentPlayer = state.currentPlayer,
                            status = state.status,
                            firstSelectedIndex = null,
                            errorMessage = null,
                        )
                    }
                }
            }
            .launchIn(viewModelScope)

        startGame(_uiState.value.settings)
    }

    // ── Game ──────────────────────────────────────

    fun onRestart() = startGame(_uiState.value.settings)

    fun onNumberClick(index: Int) {
        val current = _uiState.value
        if (current.status !is GameStatus.InProgress) return
        val selected = current.firstSelectedIndex
        when {
            selected == index ->
                _uiState.update { it.copy(firstSelectedIndex = null, errorMessage = null) }

            selected == null ->
                _uiState.update { it.copy(firstSelectedIndex = index, errorMessage = null) }

            kotlin.math.abs(selected - index) == 1 -> {
                val move = Move(leftIndex = minOf(selected, index))
                _uiState.update { it.copy(firstSelectedIndex = null, errorMessage = null) }
                viewModelScope.launch {
                    val gameState = observeGameStateUseCase().value ?: return@launch
                    val s = _uiState.value.settings
                    applyMoveUseCase(
                        state = gameState,
                        move = move,
                        aiEnabled = s.gameMode == GameMode.HUMAN_VS_AI,
                        aiAlgorithm = s.aiAlgorithm.key,
                        aiDepth = s.aiDepth,
                    )
                }
            }

            else -> _uiState.update {
                it.copy(firstSelectedIndex = index, errorMessage = "Выберите соседние числа")
            }
        }
    }

    // ── Settings ──────────────────────────────────

    fun onOpenSettings() =
        _uiState.update { it.copy(isSettingsOpen = true, draftSettings = it.settings) }

    fun onCloseSettings() =
        _uiState.update { it.copy(isSettingsOpen = false) }

    fun onSaveSettings() {
        val draft = _uiState.value.draftSettings
        _uiState.update { it.copy(settings = draft, isSettingsOpen = false) }
        startGame(draft)
    }

    fun onDraftCellCountChange(v: Int) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(cellCount = v)) }

    fun onDraftGameModeChange(v: GameMode) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(gameMode = v)) }

    fun onDraftPlayer1NameChange(v: String) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(player1Name = v)) }

    fun onDraftPlayer2NameChange(v: String) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(player2Name = v)) }

    fun onDraftFirstPlayerChange(v: PlayerId) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(firstPlayer = v)) }

    fun onDraftAlgorithmChange(v: AiAlgorithm) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(aiAlgorithm = v)) }

    fun onDraftAiDepthChange(v: Int) =
        _uiState.update { it.copy(draftSettings = it.draftSettings.copy(aiDepth = v)) }

    // ── Private ───────────────────────────────────

    private fun startGame(settings: GameSettings) {
        viewModelScope.launch { startNewGameUseCase(settings) }
    }
}

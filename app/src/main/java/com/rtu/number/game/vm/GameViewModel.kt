package com.rtu.number.game.vm

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rtu.number.game.domain.GameGraphRepository
import com.rtu.number.game.model.GameNumber
import com.rtu.number.game.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameGraphRepository: GameGraphRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        gameGraphRepository.generateGameGraph(
            players = uiState.value.players,
            maxDepth = MAX_DEPTH
        )
        viewModelScope.launch {
            gameGraphRepository.currentNode.onEach { currentNode ->
                _uiState.update {
                    it.copy(
                        splitNumber = currentNode.splitNumber,
                        players = currentNode.players,
                        currentPlayer = currentNode.players.firstOrNull { player ->
                            player.id == currentNode.currentPlayersUUID
                        }?:Player(),
                    )
                }
            }
                .collect()
        }

    }

    fun onRestart() {
        gameGraphRepository.generateGameGraph(
            players = uiState.value.players,
            maxDepth = MAX_DEPTH
        )
        _uiState.update {
            it.copy(
                players = it.players.map { player -> player.copy(score = 0) },
                firstChosenNumber = null

            )
        }
    }

    fun onNumberClick(gameNumber: GameNumber) {
        val firstChosenNumber = _uiState.value.firstChosenNumber
        if (firstChosenNumber == null) {
            _uiState.update {
                it.copy(
                    firstChosenNumber = gameNumber
                )
            }
        } else if (firstChosenNumber == gameNumber) {
            _uiState.update {
                it.copy(
                    firstChosenNumber = null
                )
            }
        } else {
            val newNumberIsNear = firstChosenNumber.index - 1 == gameNumber.index || firstChosenNumber.index + 1 == gameNumber.index
            if (newNumberIsNear) {
                makeMove(
                    Pair(
                        firstChosenNumber,
                        gameNumber
                    )
                )
            } else {
                _uiState.update {
                    it.copy(
                        firstChosenNumber = gameNumber
                    )
                }
            }
        }
    }

    private fun makeMove(stepNumbers: Pair<GameNumber, GameNumber>) {
        gameGraphRepository.makeMove(
            stepNumbers
        )
        _uiState.update {
            it.copy(
                firstChosenNumber = null
            )
        }

    }

    @Stable
    data class UiState(
        val splitNumber: List<GameNumber> = emptyList(),
        val players: List<Player> = listOf(
            Player(name = "Player 1"),
            Player(name = "Player 2")
        ),
        val currentPlayer: Player = Player(),
        val firstChosenNumber: GameNumber? = null,
    )

    companion object {
        const val MAX_DEPTH = 4
    }
}
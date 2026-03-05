package com.rtu.number.game.vm

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.rtu.number.game.model.GameNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        generateSplitNumber()
    }

    //TODO Move this logic in GameTreeRepository when it will be ready
    private fun generateSplitNumber() {
        val numberVariants = (1 .. 9).toList()
        val numberLength = Random.nextInt(
            15,
            25
        )
        val splitNumber = mutableListOf<GameNumber>()
        for (i in 0 until numberLength) {
            splitNumber.add(
                GameNumber(
                    number = numberVariants.random(),
                    index = i
                )
            )
        }

        _uiState.update {
            it.copy(
                splitNumber = splitNumber
            )
        }
    }

    fun onRestart() {
        generateSplitNumber()
        _uiState.update {
            it.copy(
                firstPlayerScore = 0,
                secondPlayerScore = 0,
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
                makeStep(
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

    private fun makeStep(stepNumbers: Pair<GameNumber, GameNumber>) {
        _uiState.update {
            it.copy(
                firstChosenNumber = null
            )
        }

    }

    @Stable
    data class UiState(
        val splitNumber: List<GameNumber> = emptyList(),
        val firstPlayerScore: Int = 0,
        val secondPlayerScore: Int = 0,
        val firstChosenNumber: GameNumber? = null
    )
}
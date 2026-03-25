package com.rtu.number.game.domain.model

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.RoundingMode

interface GameStatisticsRepository {
    val gameStatistics: StateFlow<GameStatistics>

    fun addStatistics(
        thinkingTime: Long,
        nodesEvaluated: Int,
    )

    fun clearStatistics()
}

@Singleton
class GameStatisticsRepositoryImpl @Inject constructor() : GameStatisticsRepository {

    private val gameStatisticsMutable = MutableStateFlow(
        GameStatistics()
    )
    private var moveCount: Int = 0

    override val gameStatistics: StateFlow<GameStatistics> = gameStatisticsMutable


    override fun addStatistics(
        thinkingTime: Long,
        nodesEvaluated: Int
    ) {
        val gameStatistics = gameStatisticsMutable.value
        val currentAvgThinkingTime = gameStatistics.avgThinkingTime
        val currentAvgNodesEvaluated = gameStatistics.avgNodesEvaluated
        val currentTotalNodesEvaluated = gameStatistics.totalNodesEvaluated
        moveCount++
        val newAvgThinkingTime =
            (currentAvgThinkingTime * (moveCount - 1) + thinkingTime) / moveCount
        val newAvgNodesEvaluated =
            (currentAvgNodesEvaluated * (moveCount - 1) + nodesEvaluated) / moveCount
        val newTotalNodesEvaluated = currentTotalNodesEvaluated + nodesEvaluated

        gameStatisticsMutable.value = GameStatistics(
            avgThinkingTime = newAvgThinkingTime.toBigDecimal()
                .setScale(
                    3,
                    RoundingMode.HALF_EVEN
                )
                .toDouble(),
            avgNodesEvaluated = newAvgNodesEvaluated.toBigDecimal()
                .setScale(
                    3,
                    RoundingMode.HALF_EVEN
                )
                .toDouble(),
            totalNodesEvaluated = newTotalNodesEvaluated
        )
    }

    override fun clearStatistics() {
        gameStatisticsMutable.value = GameStatistics()
        moveCount = 0
    }
}
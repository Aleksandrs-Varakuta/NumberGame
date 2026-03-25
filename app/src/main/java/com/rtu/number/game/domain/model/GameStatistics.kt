package com.rtu.number.game.domain.model

data class GameStatistics(
    val avgThinkingTime: Double = 0.0,
    val avgNodesEvaluated: Double = 0.0,
    val totalNodesEvaluated: Int = 0,
)

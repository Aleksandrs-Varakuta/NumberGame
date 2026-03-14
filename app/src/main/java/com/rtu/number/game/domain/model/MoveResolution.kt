package com.rtu.number.game.domain.model

data class MoveResolution(
    val replacementValue: Int,
    val appliedRule: ReplacementRule,
    val scoreChange: ScoreChange,
)

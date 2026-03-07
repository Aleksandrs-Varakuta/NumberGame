package com.rtu.number.game.model

import kotlinx.serialization.Serializable

@Serializable
data class GameNumber(
    val number: Int,
    val index: Int,
)
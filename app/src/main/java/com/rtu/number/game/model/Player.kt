package com.rtu.number.game.model

import kotlinx.serialization.Serializable
import java.util.UUID
@Serializable
data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Player",
    val score: Int = 0
)


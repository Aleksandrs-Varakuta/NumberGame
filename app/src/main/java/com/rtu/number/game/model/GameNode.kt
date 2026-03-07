package com.rtu.number.game.model

import kotlinx.serialization.Serializable

@Serializable
data class GameNode(
    val currentPlayersUUID: String = "",
    val splitNumber: List<GameNumber> = emptyList(),
    val players: List<Player> = emptyList(),
) {
    val children: MutableList<GameNode> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameNode) return false

        return currentPlayersUUID == other.currentPlayersUUID && splitNumber == other.splitNumber && players == other.players
    }

    override fun hashCode(): Int {
        var result = currentPlayersUUID.hashCode()
        result = 31 * result + splitNumber.hashCode()
        result = 31 * result + players.hashCode()
        return result
    }
}

fun GameNode.getNextPlayersUUID(): String {
    val playerUIIDS = players.map { it.id }
    if (playerUIIDS.isEmpty()) return ""
    if (playerUIIDS.contains(currentPlayersUUID)) {
        val currentPlayerIndex = playerUIIDS.indexOf(currentPlayersUUID)
        val nextPlayerIndex = (currentPlayerIndex + 1) % players.size
        return playerUIIDS[nextPlayerIndex]
    } else {
        return playerUIIDS.firstOrNull()?:""
    }
}


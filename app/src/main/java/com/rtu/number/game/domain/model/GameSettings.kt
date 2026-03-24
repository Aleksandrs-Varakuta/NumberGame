package com.rtu.number.game.domain.model

data class GameSettings(
    val cellCount: Int = MIN_CELL_COUNT,
    val gameMode: GameMode = GameMode.HUMAN_VS_AI,
    val player1Name: String = "Player 1",
    val player2Name: String = "Player 2",
    val firstPlayer: PlayerId = PlayerId.FIRST,
    val aiAlgorithm: AiAlgorithm = AiAlgorithm.ALPHA_BETA,
    val aiDepth: Int = MIN_AI_DEPTH,
)

enum class GameMode {
    HUMAN_VS_HUMAN,
    HUMAN_VS_AI,
}

enum class AiAlgorithm() {
    MINIMAX,
    ALPHA_BETA,
}

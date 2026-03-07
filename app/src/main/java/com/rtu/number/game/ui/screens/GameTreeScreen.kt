package com.rtu.number.game.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.rtu.number.game.model.GameNode

@Composable
fun GameGraphScreen(
    contentPadding: PaddingValues,
    displayableGraph: List<List<GameNode>>,
    onExpandNodeBox: (gameNode: GameNode) -> Unit = {},
) { //TODO Create API to build graph on server and then show here
}
package com.rtu.number.game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.GameMode
import com.rtu.number.game.domain.model.GameSettings
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.vm.GameViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    uiState: GameViewModel.UiState,
    onRestart: () -> Unit,
    onNumberClick: (Int) -> Unit,
    onOpenSettings: () -> Unit,
    onCloseSettings: () -> Unit,
    onSaveSettings: () -> Unit,
    onDraftCellCountChange: (Int) -> Unit,
    onDraftGameModeChange: (GameMode) -> Unit,
    onDraftPlayer1NameChange: (String) -> Unit,
    onDraftPlayer2NameChange: (String) -> Unit,
    onDraftFirstPlayerChange: (PlayerId) -> Unit,
    onDraftAlgorithmChange: (AiAlgorithm) -> Unit,
    onDraftAiDepthChange: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Header(
            firstPlayerName = uiState.player1Name,
            secondPlayerName = uiState.player2Name,
            firstPlayerScore = uiState.firstPlayerScore,
            secondPlayerScore = uiState.secondPlayerScore,
            currentPlayer = uiState.currentPlayer,
        )

        Spacer(Modifier.height(16.dp))

        GameStateInfo(
            status = uiState.status,
            player1Name = uiState.player1Name,
            player2Name = uiState.player2Name,
            errorMessage = uiState.errorMessage,
        )

        Spacer(Modifier.height(24.dp))

        NumberRow(
            numbers = uiState.numbers,
            firstSelectedIndex = uiState.firstSelectedIndex,
            onNumberClick = onNumberClick,
            isInteractionEnabled = uiState.status == GameStatus.InProgress,
        )

        Spacer(Modifier.weight(1f))

        BottomBar(onRestart = onRestart, onOpenSettings = onOpenSettings)
    }

    if (uiState.isSettingsOpen) {
        SettingsDialog(
            draft = uiState.draftSettings,
            onDismiss = onCloseSettings,
            onSave = onSaveSettings,
            onCellCountChange = onDraftCellCountChange,
            onGameModeChange = onDraftGameModeChange,
            onPlayer1NameChange = onDraftPlayer1NameChange,
            onPlayer2NameChange = onDraftPlayer2NameChange,
            onFirstPlayerChange = onDraftFirstPlayerChange,
            onAlgorithmChange = onDraftAlgorithmChange,
            onAiDepthChange = onDraftAiDepthChange,
        )
    }
}

@Composable
private fun Header(
    firstPlayerName: String,
    secondPlayerName: String,
    firstPlayerScore: Int,
    secondPlayerScore: Int,
    currentPlayer: PlayerId,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayerScore(
            title = firstPlayerName,
            score = firstPlayerScore,
            isCurrent = currentPlayer == PlayerId.FIRST,
        )
        Text("vs", style = TextStyle(fontSize = 14.sp, color = Color.White))
        PlayerScore(
            title = secondPlayerName,
            score = secondPlayerScore,
            isCurrent = currentPlayer == PlayerId.SECOND,
        )
    }
}

@Composable
private fun PlayerScore(title: String, score: Int, isCurrent: Boolean) {
    Text(
        text = "$title: $score",
        style = TextStyle(
            fontWeight = if (isCurrent) FontWeight.W700 else FontWeight.Normal,
            color = Color.White,
            fontSize = 16.sp,
        ),
    )
}

@Composable
private fun GameStateInfo(
    status: GameStatus,
    player1Name: String,
    player2Name: String,
    errorMessage: String?,
) {
    val statusText = when (status) {
        GameStatus.InProgress -> "Game in progress"
        is GameStatus.Finished -> when (status.winner) {
            PlayerId.FIRST  -> "Winner: $player1Name"
            PlayerId.SECOND -> "Winner: $player2Name"
            null            -> "Draw"
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = statusText, style = TextStyle(color = Color.White))
        if (!errorMessage.isNullOrBlank()) {
            Text(text = errorMessage, color = Color.Red)
        }
    }
}

@Composable
private fun NumberRow(
    numbers: List<Int>,
    firstSelectedIndex: Int?,
    onNumberClick: (Int) -> Unit,
    isInteractionEnabled: Boolean,
) {
    val itemSpacing = 8.dp
    val itemSize = 48.dp
    var containerWidthPx by remember { mutableIntStateOf(0) }

    val indexedNumbers = remember(numbers) {
        numbers.mapIndexed { i, v -> IndexedNumber(i, v) }
    }
    val numberRows = rememberNumberRows(indexedNumbers, containerWidthPx, itemSize, itemSpacing)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { containerWidthPx = it.size.width },
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        numberRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                row.forEach { item ->
                    val isSelected = firstSelectedIndex == item.index
                    val isNeighbour = firstSelectedIndex != null &&
                            kotlin.math.abs(firstSelectedIndex - item.index) == 1
                    Box(
                        modifier = Modifier
                            .size(itemSize)
                            .numberItemModifier(isSelected, isNeighbour)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                enabled = isInteractionEnabled,
                            ) { onNumberClick(item.index) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = item.value.toString(), color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberNumberRows(
    items: List<IndexedNumber>,
    containerWidthPx: Int,
    itemSize: Dp,
    itemSpacing: Dp,
): List<List<IndexedNumber>> {
    val density = LocalDensity.current
    return remember(items, containerWidthPx) {
        with(density) {
            if (items.isEmpty() || containerWidthPx <= 0) emptyList()
            else {
                val maxItems = maxOf(
                    1,
                    ((containerWidthPx + itemSpacing.toPx()) /
                            (itemSize.toPx() + itemSpacing.toPx())).toInt()
                )
                items.chunked(maxItems)
            }
        }
    }
}

@Composable
private fun BottomBar(onRestart: () -> Unit, onOpenSettings: () -> Unit) {
    val btnH = 48.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(modifier = Modifier.height(btnH), onClick = onRestart) { Text("Restart") }

        Button(
            modifier = Modifier.size(btnH),
            onClick = onOpenSettings,
            contentPadding = PaddingValues(4.dp),
            shape = CircleShape,
        ) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

private fun Modifier.numberItemModifier(isSelected: Boolean, isNeighbour: Boolean): Modifier {
    val border = when { isSelected -> Color(0xFF1565C0); isNeighbour -> Color(0xFF42A5F5); else -> Color(0xFFBDBDBD) }
    val bg     = when { isSelected -> Color(0xFFBBDEFB); isNeighbour -> Color(0xFFE3F2FD); else -> Color(0xFFF5F5F5) }
    return this
        .background(color = bg, shape = RoundedCornerShape(12.dp))
        .border(width = 2.dp, color = border, shape = RoundedCornerShape(12.dp))
}

private data class IndexedNumber(val index: Int, val value: Int)

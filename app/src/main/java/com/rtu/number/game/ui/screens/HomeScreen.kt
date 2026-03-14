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
import androidx.compose.material.icons.filled.Info
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
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.vm.GameViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    uiState: GameViewModel.UiState,
    onRestart: () -> Unit,
    onNumberClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
    ) {
        Header(
            firstPlayerScore = uiState.firstPlayerScore,
            secondPlayerScore = uiState.secondPlayerScore,
            currentPlayer = uiState.currentPlayer,
        )

        Spacer(modifier = Modifier.height(16.dp))

        GameStateInfo(
            status = uiState.status,
            errorMessage = uiState.errorMessage,
        )

        Spacer(modifier = Modifier.height(24.dp))

        NumberRow(
            numbers = uiState.numbers,
            firstSelectedIndex = uiState.firstSelectedIndex,
            onNumberClick = onNumberClick,
            isInteractionEnabled = uiState.status == GameStatus.InProgress,
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomBar(
            onRestart = onRestart,
        )
    }
}

@Composable
private fun Header(
    firstPlayerScore: Int,
    secondPlayerScore: Int,
    currentPlayer: PlayerId,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            12.dp,
            Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PlayerScore(
            title = "Player 1",
            score = firstPlayerScore,
            isCurrent = currentPlayer == PlayerId.FIRST,
        )
        PlayerScore(
            title = "Player 2",
            score = secondPlayerScore,
            isCurrent = currentPlayer == PlayerId.SECOND,
        )
    }
}

@Composable
private fun PlayerScore(
    title: String,
    score: Int,
    isCurrent: Boolean,
) {
    Text(
        text = "$title: $score",
        style = TextStyle(
            fontWeight = if (isCurrent) FontWeight.W700 else FontWeight.Normal,
        ),
    )
}

@Composable
private fun GameStateInfo(
    status: GameStatus,
    errorMessage: String?,
) {
    val statusText = when (status) {
        GameStatus.InProgress -> "Game in progress"
        is GameStatus.Finished -> {
            when (status.winner) {
                PlayerId.FIRST -> "Winner: Player 1"
                PlayerId.SECOND -> "Winner: Player 2"
                null -> "Draw"
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = statusText)

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
            )
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
        numbers.mapIndexed { index, value ->
            IndexedNumber(
                index = index,
                value = value,
            )
        }
    }

    val numberRows = rememberNumberRows(
        items = indexedNumbers,
        containerWidthPx = containerWidthPx,
        itemSize = itemSize,
        itemSpacing = itemSpacing,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                containerWidthPx = coordinates.size.width
            },
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        numberRows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    itemSpacing,
                    Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                row.forEach { item ->
                    val isSelected = firstSelectedIndex == item.index
                    val isNeighbourCandidate = firstSelectedIndex != null && kotlin.math.abs(firstSelectedIndex - item.index) == 1

                    Box(
                        modifier = Modifier
                            .size(itemSize)
                            .numberItemModifier(
                                isSelected = isSelected,
                                isNeighbourCandidate = isNeighbourCandidate,
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                enabled = isInteractionEnabled,
                            ) {
                                onNumberClick(item.index)
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = item.value.toString(),
                            color = Color.Black,
                        )
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

    return remember(
        items,
        containerWidthPx
    ) {
        with(density) {
            if (items.isEmpty() || containerWidthPx <= 0) {
                emptyList()
            } else {
                val rowWidthWithSpacing = containerWidthPx + itemSpacing.toPx()
                val itemSizeWithSpacing = itemSize.toPx() + itemSpacing.toPx()
                val hypotheticalItemsInRow = (rowWidthWithSpacing / itemSizeWithSpacing).toInt()
                val maxItemsInRow = maxOf(
                    1,
                    hypotheticalItemsInRow
                )

                items.chunked(maxItemsInRow)
            }
        }
    }
}

@Composable
private fun BottomBar(
    onRestart: () -> Unit,
) {
    val buttonHeight = 48.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier.height(buttonHeight),
            onClick = {},
        ) {
            Text(text = "AI Model")
        }

        Button(
            modifier = Modifier.height(buttonHeight),
            onClick = onRestart,
        ) {
            Text(text = "Restart")
        }

        Button(
            modifier = Modifier.size(buttonHeight),
            onClick = {},
            contentPadding = PaddingValues(4.dp),
            shape = CircleShape,
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
            )
        }

        Button(
            modifier = Modifier.size(buttonHeight),
            onClick = {},
            contentPadding = PaddingValues(4.dp),
            shape = CircleShape,
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
            )
        }

    }
}

private fun Modifier.numberItemModifier(
    isSelected: Boolean,
    isNeighbourCandidate: Boolean,
): Modifier {
    val borderColor = when {
        isSelected -> Color(0xFF1565C0)
        isNeighbourCandidate -> Color(0xFF42A5F5)
        else -> Color(0xFFBDBDBD)
    }

    val backgroundColor = when {
        isSelected -> Color(0xFFBBDEFB)
        isNeighbourCandidate -> Color(0xFFE3F2FD)
        else -> Color(0xFFF5F5F5)
    }

    return this
        .background(
            color = backgroundColor,
            shape = RoundedCornerShape(12.dp),
        )
        .border(
            width = 2.dp,
            color = borderColor,
            shape = RoundedCornerShape(12.dp),
        )
}

private data class IndexedNumber(
    val index: Int,
    val value: Int,
)
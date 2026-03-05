package com.rtu.number.game.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rtu.number.game.model.GameNumber
import com.rtu.number.game.ui.extension.getNumberBoxDesignModifier
import com.rtu.number.game.vm.HomeViewModel

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    uiState: HomeViewModel.UiState,
    onRestart: () -> Unit,
    onNumberClick: (GameNumber) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Header(
            firstPlayerScore = uiState.firstPlayerScore,
            secondPlayerScore = uiState.secondPlayerScore
        )
        Spacer(Modifier.height(32.dp))
        NumberRow(
            splitNumber = uiState.splitNumber,
            onNumberClick = onNumberClick,
            firstChosenNumber = uiState.firstChosenNumber
        )
        Spacer(Modifier.weight(1f))
        BottomBar(onRestart = onRestart)
    }
}

@Composable
fun Header(
    firstPlayerScore: Int,
    secondPlayerScore: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            12.dp,
            Alignment.CenterHorizontally
        ),
    ) {
        Text(
            text = "First player's score: $firstPlayerScore"
        )
        Text(
            text = "Second player's score: $secondPlayerScore"
        )
    }
}

@Composable
fun NumberRow(
    splitNumber: List<GameNumber>,
    onNumberClick: (GameNumber) -> Unit,
    firstChosenNumber: GameNumber?
) {
    val itemSpacing = 8.dp
    val itemSize = 48.dp

    var containerWidthPx by remember { mutableIntStateOf(0) }

    val numberRows = rememberNumberRows(
        items = splitNumber,
        containerWidthPx = containerWidthPx,
        itemSize = itemSize,
        itemSpacing = itemSpacing
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                containerWidthPx = coordinates.size.width
            },
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        numberRows.forEach { row ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    itemSpacing,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { gameNumber ->
                    Box(
                        modifier = Modifier
                            .size(itemSize)
                            .getNumberBoxDesignModifier(
                                firstChosenNumber = firstChosenNumber,
                                currentNumber = gameNumber
                            )
                            .clickable {
                                onNumberClick(gameNumber)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = gameNumber.number.toString(),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberNumberRows(
    items: List<GameNumber>,
    containerWidthPx: Int,
    itemSize: Dp,
    itemSpacing: Dp
): List<List<GameNumber>> {
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
fun BottomBar(
    onRestart: () -> Unit
) {

    val buttonHeight = 48.dp
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Button(
            modifier = Modifier.height(buttonHeight),
            onClick = {}) {
            Text(
                text = "AI Model"
            )
        }
        Button(
            modifier = Modifier.height(buttonHeight),
            onClick = onRestart
        ) {
            Text(
                text = "Restart"
            )
        }

        Button(
            modifier = Modifier.size(buttonHeight),
            onClick = {},
            contentPadding = PaddingValues(4.dp),
            shape = CircleShape

        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        }
        Button(
            modifier = Modifier.size(buttonHeight),
            onClick = {},
            contentPadding = PaddingValues(4.dp),
            shape = CircleShape

        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )
        }

    }
}

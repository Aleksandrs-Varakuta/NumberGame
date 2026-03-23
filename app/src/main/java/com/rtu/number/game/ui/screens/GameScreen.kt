package com.rtu.number.game.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtu.number.game.R
import com.rtu.number.game.domain.model.GameMode
import com.rtu.number.game.domain.model.GameStatus
import com.rtu.number.game.domain.model.PlayerId
import com.rtu.number.game.ui.component.NumberRow
import com.rtu.number.game.vm.GameViewModel

@Composable
fun GameScreen(
    contentPadding: PaddingValues,
    uiState: GameViewModel.UiState,
    onRestart: () -> Unit,
    onNumberClick: (Int) -> Unit,
    onMoveAnimationFinished: () -> Unit,
    onBack: () -> Unit,
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
        GameStateInfo(
            status = uiState.status,
            player1Name = uiState.player1Name,
            player2Name = uiState.player2Name,
        )

        Spacer(Modifier.height(24.dp))

        NumberRow(
            numbers = uiState.numbers,
            firstSelectedIndex = uiState.firstSelectedIndex,
            onNumberClick = onNumberClick,
            isInteractionEnabled = uiState.canInteract,
            moveToAnimate = uiState.moveToAnimate,
            onMoveAnimationFinished = onMoveAnimationFinished,
        )



        BottomBar(
            onRestart = onRestart,
            onOpenInfo = {},
            onBack = onBack,
            firstPlayerName = uiState.player1Name,
            secondPlayerName = uiState.player2Name,
            firstPlayerScore = uiState.firstPlayerScore,
            secondPlayerScore = uiState.secondPlayerScore,
            currentPlayer = uiState.currentPlayer,
            gameMode = uiState.settings.gameMode,
            aiThinks = uiState.isAiTurn
        )
    }
}

@Composable
private fun GameStateInfo(
    status: GameStatus,
    player1Name: String,
    player2Name: String,
) {
    val statusText = when (status) {
        GameStatus.InProgress -> "Game in progress"
        is GameStatus.Finished -> when (status.winner) {
            PlayerId.FIRST -> "Winner: $player1Name"
            PlayerId.SECOND -> "Winner: $player2Name"
            null -> "Draw"
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = statusText,
            style = TextStyle(color = Color.White)
        )
    }
}

@Composable
private fun BottomBar(
    onRestart: () -> Unit,
    onOpenInfo: () -> Unit,
    onBack: () -> Unit,
    firstPlayerName: String,
    secondPlayerName: String,
    firstPlayerScore: Int,
    secondPlayerScore: Int,
    currentPlayer: PlayerId,
    gameMode: GameMode,
    aiThinks: Boolean = true
) {
    val btnH = 48.dp
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerScore(
                title = firstPlayerName,
                score = firstPlayerScore,
                isCurrent = currentPlayer == PlayerId.FIRST,
            )
            Image(
                modifier = Modifier.size(150.dp),
                painter = painterResource(R.drawable.blue_alien),
                contentDescription = null,
            )
        }
        IconButton(
            modifier = Modifier,
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
        Button(
            modifier = Modifier.height(btnH),
            onClick = onRestart
        ) { Text("Restart") }

        Image(
            modifier = Modifier
                .size(btnH)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onOpenInfo
                ),
            painter = painterResource(R.drawable.info_button),
            contentDescription = null
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerScore(
                title = secondPlayerName,
                score = secondPlayerScore,
                isCurrent = currentPlayer == PlayerId.SECOND,
            )
            Box(contentAlignment = Alignment.TopCenter) {
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(
                        if (gameMode == GameMode.HUMAN_VS_HUMAN) {
                            R.drawable.beige_alien
                        } else {
                            R.drawable.beige_alien_robot
                        }
                    ),
                    contentDescription = null,
                )
                if (aiThinks) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(20.dp),
                        color = Color.Black,
                    )
                }
            }

        }
    }
}

@Composable
private fun PlayerScore(
    title: String,
    score: Int,
    isCurrent: Boolean
) {
    Text(
        text = "$title: $score",
        style = TextStyle(
            fontWeight = if (isCurrent) FontWeight.W700 else FontWeight.Normal,
            color = Color.White,
            fontSize = 16.sp,
        ),
    )
}

package com.rtu.number.game.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.GameSettings
import com.rtu.number.game.domain.model.MAX_AI_DEPTH
import com.rtu.number.game.domain.model.MAX_CELL_COUNT
import com.rtu.number.game.domain.model.MIN_AI_DEPTH
import com.rtu.number.game.domain.model.MIN_CELL_COUNT
import com.rtu.number.game.domain.model.PlayerId

@Composable
fun SettingsScreen(
    contentPadding: PaddingValues,
    settings: GameSettings,
    onBack: () -> Unit,
    onChangeCellCount: (Int) -> Unit,
    onChangePlayer1Name: (String) -> Unit,
    onChangePlayer2Name: (String) -> Unit,
    onChangeFirstPlayer: (PlayerId) -> Unit,
    onChangeAlgorithm: (AiAlgorithm) -> Unit,
    onChangeAiDepth: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlobalSettingsColumn(
            settings = settings,
            onChangeCellCount = onChangeCellCount,
            onChangePlayer1Name = onChangePlayer1Name,
            onChangePlayer2Name = onChangePlayer2Name,
            onChangeFirstPlayer = onChangeFirstPlayer,
            onBack = onBack
        )
        AiSettingsColumn(
            onChangeAlgorithm = onChangeAlgorithm,
            onChangeAiDepth = onChangeAiDepth,
            algorithm = settings.aiAlgorithm,
            depth = settings.aiDepth

        )

    }

}

@Composable
private fun GlobalSettingsColumn(
    settings: GameSettings,
    onChangeCellCount: (Int) -> Unit,
    onChangePlayer1Name: (String) -> Unit,
    onChangePlayer2Name: (String) -> Unit,
    onChangeFirstPlayer: (PlayerId) -> Unit,
    onBack: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                text = "Global Settings",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
        CellCountSettings(
            onChangeCellCount = onChangeCellCount,
            cellCount = settings.cellCount
        )
        PlayerSettings(
            player1Name = settings.player1Name,
            player2Name = settings.player2Name,
            firstPlayer = settings.firstPlayer,
            onChangePlayer1Name = onChangePlayer1Name,
            onChangePlayer2Name = onChangePlayer2Name,
            onChangeFirstPlayer = onChangeFirstPlayer
        )

    }

}

@Composable
private fun CellCountSettings(
    onChangeCellCount: (Int) -> Unit,
    cellCount: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Cell count",
            style = TextStyle(fontSize = 18.sp)
        )
        Slider(
            modifier = Modifier.size(
                width = 220.dp,
                height = 25.dp
            ),
            value = cellCount.toFloat(),
            onValueChange = { onChangeCellCount(it.toInt()) },
            steps = MAX_CELL_COUNT - MIN_CELL_COUNT,
            valueRange = MIN_CELL_COUNT.toFloat()..MAX_CELL_COUNT.toFloat()
        )
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(4.dp),
            text = "$cellCount",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
private fun PlayerSettings(
    player1Name: String,
    player2Name: String,
    firstPlayer: PlayerId,
    onChangePlayer1Name: (String) -> Unit,
    onChangePlayer2Name: (String) -> Unit,
    onChangeFirstPlayer: (PlayerId) -> Unit,
) {
    Column {
        PlayerNamesSettings(
            player1Name = player1Name,
            player2Name = player2Name,
            onChangePlayer1Name = onChangePlayer1Name,
            onChangePlayer2Name = onChangePlayer2Name
        )
        FirstPlayerSettings(
            firstPlayer = firstPlayer,
            onChangeFirstPlayer = onChangeFirstPlayer
        )

    }
}

@Composable
private fun PlayerNamesSettings(
    player1Name: String,
    player2Name: String,
    onChangePlayer1Name: (String) -> Unit,
    onChangePlayer2Name: (String) -> Unit,
) {
    Column {
        Text(
            text = "Player names",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC72EEE)
            )
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier.padding(start = 24.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "P1",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                )
                BasicTextField(
                    value = player1Name,
                    onValueChange = onChangePlayer1Name,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.width(200.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                        }
                    })
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "P2",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                )
                BasicTextField(
                    value = player2Name,
                    onValueChange = onChangePlayer2Name,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.width(200.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                        }
                    })
            }
        }
    }

}

@Composable
private fun FirstPlayerSettings(
    firstPlayer: PlayerId,
    onChangeFirstPlayer: (PlayerId) -> Unit,
) {
    Column {
        Text(
            text = "Game starts",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
        )
        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(start = 24.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "P1",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                )
                Checkbox(
                    modifier = Modifier.size(30.dp),
                    checked = firstPlayer == PlayerId.FIRST,
                    onCheckedChange = { onChangeFirstPlayer(if (it) PlayerId.FIRST else PlayerId.SECOND) })
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "P2",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                )
                Checkbox(
                    modifier = Modifier.size(30.dp),
                    checked = firstPlayer == PlayerId.SECOND,
                    onCheckedChange = { onChangeFirstPlayer(if (it) PlayerId.SECOND else PlayerId.FIRST) })
            }
        }
    }
}


@Composable
private fun AiSettingsColumn(
    depth: Int,
    algorithm: AiAlgorithm,
    onChangeAlgorithm: (AiAlgorithm) -> Unit,
    onChangeAiDepth: (Int) -> Unit
) {
    Column(
        modifier = Modifier.width(400.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "AI Settings",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Yellow
            )
        )
        Spacer(Modifier.height(16.dp))
        ForwardForecastSetting(
            onChangeDepth = onChangeAiDepth,
            depth = depth
        )
        Spacer(Modifier.height(8.dp))
        AiAlgorithmSetting(
            algorithm = algorithm,
            onChangeAlgorithm = onChangeAlgorithm
        )

    }

}

@Composable
private fun ForwardForecastSetting(
    onChangeDepth: (Int) -> Unit,
    depth: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Forward forecast",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Cyan
            )
        )
        Slider(
            modifier = Modifier.size(
                width = 220.dp,
                height = 25.dp
            ),
            value = depth.toFloat(),
            onValueChange = { onChangeDepth(it.toInt()) },
            steps = MAX_AI_DEPTH - MIN_AI_DEPTH,
            valueRange = MIN_AI_DEPTH.toFloat()..MAX_AI_DEPTH.toFloat()
        )
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(4.dp),
            text = "$depth",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
private fun AiAlgorithmSetting(
    algorithm: AiAlgorithm,
    onChangeAlgorithm: (AiAlgorithm) -> Unit
) {
    Column {
        Text(
            text = "Algorithm",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
        )
        Spacer(Modifier.height(6.dp))

        Column(
            modifier = Modifier
                .padding(start = 24.dp)
                .width(130.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "MiniMax",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    )
                )
                Checkbox(
                    modifier = Modifier.size(30.dp),
                    checked = algorithm == AiAlgorithm.MINIMAX,
                    onCheckedChange = { onChangeAlgorithm(if (it) AiAlgorithm.MINIMAX else AiAlgorithm.ALPHA_BETA) })
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Alpha-Beta",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Checkbox(
                    modifier = Modifier.size(30.dp),
                    checked = algorithm == AiAlgorithm.ALPHA_BETA,
                    onCheckedChange = { onChangeAlgorithm(if (it) AiAlgorithm.ALPHA_BETA else AiAlgorithm.MINIMAX) })
            }
        }
    }
}
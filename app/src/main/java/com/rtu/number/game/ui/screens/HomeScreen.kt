package com.rtu.number.game.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rtu.number.game.R
import com.rtu.number.game.domain.model.GameMode

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    onStartGame: (gameMode: GameMode) -> Unit,
    onOpenSettings: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            Alignment.CenterHorizontally
        )
    ) {
        Image(
            modifier = Modifier
                .size(
                    width = 200.dp,
                    height = 100.dp
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onStartGame(GameMode.HUMAN_VS_HUMAN) }),
            painter = painterResource(R.drawable.p_v_p_button),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .size(
                    width = 200.dp,
                    height = 100.dp
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onStartGame(GameMode.HUMAN_VS_AI) }),
            painter = painterResource(R.drawable.p_v_ai_button),
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(90.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onOpenSettings
                ),
            painter = painterResource(R.drawable.settings_button),
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(90.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { }),
            painter = painterResource(R.drawable.info_button),
            contentDescription = null
        )

    }

}


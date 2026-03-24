package com.rtu.number.game.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rtu.number.game.R
import com.rtu.number.game.domain.model.GameMode
import com.rtu.number.game.ui.component.ImageButton
import com.rtu.number.game.ui.component.InfoDialog

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    onStartGame: (gameMode: GameMode) -> Unit,
    onOpenSettings: () -> Unit,
) {
    var showInfoDialog by remember { mutableStateOf(false) }

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

        ImageButton(
            size = DpSize(
                width = 200.dp,
                height = 100.dp
            ),
            imageRes = R.drawable.p_v_p_button,
            onClick = { onStartGame(GameMode.HUMAN_VS_HUMAN) })

        ImageButton(
            size = DpSize(
                width = 200.dp,
                height = 100.dp
            ),
            imageRes = R.drawable.p_v_ai_button,
            onClick = { onStartGame(GameMode.HUMAN_VS_AI) })

        ImageButton(
            size = 90.dp,
            imageRes = R.drawable.settings_button,
            onClick = onOpenSettings
        )

        ImageButton(
            size = 90.dp,
            imageRes = R.drawable.info_button,
            onClick = { showInfoDialog = true })


        if (showInfoDialog) {
            InfoDialog(onDismiss = { showInfoDialog = false })
        }
    }

}


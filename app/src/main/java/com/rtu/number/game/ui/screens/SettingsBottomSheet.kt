package com.rtu.number.game.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rtu.number.game.domain.model.AiAlgorithm
import com.rtu.number.game.domain.model.GameMode
import com.rtu.number.game.domain.model.GameSettings
import com.rtu.number.game.domain.model.PlayerId

private val TextDark = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF666666)
private val DividerColor = Color(0xFFE0E0E0)
private val CardBg = Color(0xFFFAFAFA)

@Composable
fun SettingsDialog(
    draft: GameSettings,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onCellCountChange: (Int) -> Unit,
    onGameModeChange: (GameMode) -> Unit,
    onPlayer1NameChange: (String) -> Unit,
    onPlayer2NameChange: (String) -> Unit,
    onFirstPlayerChange: (PlayerId) -> Unit,
    onAlgorithmChange: (AiAlgorithm) -> Unit,
    onAiDepthChange: (Int) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        // Оборачиваем весь диалог — все Text внутри получат тёмный цвет по умолчанию
        CompositionLocalProvider(
            LocalContentColor provides TextDark,
            LocalTextStyle provides TextStyle(color = TextDark),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                ) {
                    Text(
                        text = "⚙️  Global Settings",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                    )

                    Spacer(Modifier.height(20.dp))

                    // ── Cell count ───────────────────────────
                    SettingSection(title = "Cell count: ${draft.cellCount}") {
                        Slider(
                            value = draft.cellCount.toFloat(),
                            onValueChange = { onCellCountChange(it.toInt()) },
                            valueRange = 15f..25f,
                            steps = 9,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("15", fontSize = 12.sp, color = TextSecondary)
                            Text("25", fontSize = 12.sp, color = TextSecondary)
                        }
                    }

                    Divider()

                    // ── Game mode ────────────────────────────
                    SettingSection(title = "Game against") {
                        Row(Modifier.selectableGroup(), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = draft.gameMode == GameMode.HUMAN_VS_HUMAN,
                                onClick = { onGameModeChange(GameMode.HUMAN_VS_HUMAN) },
                            )
                            Text("2 Players", color = TextDark)
                            Spacer(Modifier.width(32.dp))
                            RadioButton(
                                selected = draft.gameMode == GameMode.HUMAN_VS_AI,
                                onClick = { onGameModeChange(GameMode.HUMAN_VS_AI) },
                            )
                            Text("AI", color = TextDark)
                        }
                    }

                    Divider()

                    // ── Player names ─────────────────────────
                    SettingSection(title = "Player names") {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = draft.player1Name,
                            onValueChange = onPlayer1NameChange,
                            label = { Text("Player 1", color = TextSecondary) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = draft.player2Name,
                            onValueChange = onPlayer2NameChange,
                            label = {
                                Text(
                                    if (draft.gameMode == GameMode.HUMAN_VS_AI) "AI name" else "Player 2",
                                    color = TextSecondary,
                                )
                            },
                            singleLine = true,
                            enabled = draft.gameMode == GameMode.HUMAN_VS_HUMAN,
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        )
                    }

                    Divider()

                    // ── Who starts ───────────────────────────
                    SettingSection(title = "Game starts") {
                        Row(Modifier.selectableGroup(), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = draft.firstPlayer == PlayerId.FIRST,
                                onClick = { onFirstPlayerChange(PlayerId.FIRST) },
                            )
                            Text(draft.player1Name.ifBlank { "Player 1" }, color = TextDark)
                            Spacer(Modifier.width(32.dp))
                            RadioButton(
                                selected = draft.firstPlayer == PlayerId.SECOND,
                                onClick = { onFirstPlayerChange(PlayerId.SECOND) },
                            )
                            Text(draft.player2Name.ifBlank { "Player 2" }, color = TextDark)
                        }
                    }

                    // ── AI-only settings ─────────────────────
                    if (draft.gameMode == GameMode.HUMAN_VS_AI) {

                        Divider()

                        SettingSection(title = "AI Algorithm") {
                            Row(Modifier.selectableGroup(), verticalAlignment = Alignment.CenterVertically) {
                                AiAlgorithm.entries.forEach { algo ->
                                    RadioButton(
                                        selected = draft.aiAlgorithm == algo,
                                        onClick = { onAlgorithmChange(algo) },
                                    )
                                    Text(algo.label, color = TextDark)
                                    Spacer(Modifier.width(24.dp))
                                }
                            }
                        }

                        Divider()

                        SettingSection(title = "Search depth: ${draft.aiDepth}") {
                            Slider(
                                value = draft.aiDepth.toFloat(),
                                onValueChange = { onAiDepthChange(it.toInt()) },
                                valueRange = 1f..4f,
                                steps = 2,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("1 — Быстро", fontSize = 12.sp, color = TextSecondary)
                                Text("4 — Медленно", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // ── Buttons ──────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedButton(modifier = Modifier.weight(1f), onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Button(modifier = Modifier.weight(1f), onClick = onSave) {
                            Text("Save & Restart")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextDark)
        content()
    }
}

@Composable
private fun Divider() = HorizontalDivider(color = DividerColor)

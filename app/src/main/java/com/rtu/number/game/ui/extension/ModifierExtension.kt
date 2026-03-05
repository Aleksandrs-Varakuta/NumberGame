package com.rtu.number.game.ui.extension

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rtu.number.game.model.GameNumber

fun Modifier.getNumberBoxDesignModifier(
    firstChosenNumber: GameNumber?,
    currentNumber: GameNumber
): Modifier {
    val isChosenNumber = firstChosenNumber == currentNumber
    val isNearChosenNumber = firstChosenNumber?.index == currentNumber.index - 1 || firstChosenNumber?.index == currentNumber.index + 1
    return clip(CircleShape)
        .background(
            color = if (isChosenNumber || firstChosenNumber == null) {
                Color.LightGray
            } else if (isNearChosenNumber) {
                Color.LightGray.copy(alpha = 0.8f)
            } else {
                Color.LightGray.copy(alpha = 0.4f)
            },
        )
        .border(
            width = 1.dp,
            color = if (isNearChosenNumber || isChosenNumber) {
                Color.White
            } else {
                Color.Transparent
            },
            shape = CircleShape
        )
}
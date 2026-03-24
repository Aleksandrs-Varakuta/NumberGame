package com.rtu.number.game.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@Composable
fun ImageButton(
    size: Dp,
    @DrawableRes
    imageRes: Int,
    onClick: () -> Unit,
) {
    ImageButton(
        size = DpSize(
            size,
            size
        ),
        imageRes = imageRes,
        onClick = onClick
    )
}

@Composable
fun ImageButton(
    size: DpSize,
    @DrawableRes
    imageRes: Int,
    onClick: () -> Unit,
) {
    var buttonScale by remember { mutableFloatStateOf(1f) }
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(size * buttonScale)
                .pointerInput(
                    Unit
                ) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.changes.any { it.pressed }) {
                                buttonScale = 0.9f
                            } else {
                                buttonScale = 1f
                                onClick()
                            }

                        }
                    }
                },
            painter = painterResource(imageRes),
            contentDescription = null
        )
    }
}


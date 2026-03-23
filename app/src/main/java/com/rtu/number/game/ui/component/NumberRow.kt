package com.rtu.number.game.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.rtu.number.game.domain.model.Move
import kotlin.math.roundToInt

@Composable
fun NumberRow(
    numbers: List<Int>,
    firstSelectedIndex: Int?,
    onNumberClick: (Int) -> Unit,
    isInteractionEnabled: Boolean,
    moveToAnimate: Move?,
    onMoveAnimationFinished: () -> Unit,
) {
    val itemSpacing = 8.dp
    val itemSize = 48.dp

    val density = LocalDensity.current
    val itemSizePx = with(density) { itemSize.toPx() }
    val itemSpacingPx = with(density) { itemSpacing.toPx() }

    var containerWidthPx by remember { mutableIntStateOf(0) }
    val animationProgress = remember { Animatable(0f) }

    val currentItems = remember(numbers) {
        numbers.mapIndexed { index, value ->
            IndexedNumber(
                index = index,
                value = value,
            )
        }
    }

    val leftIndex = moveToAnimate?.leftIndex?.takeIf { it in numbers.indices }
    val rightIndex = leftIndex?.plus(1)
        ?.takeIf { it in numbers.indices }

    val currentLayout = remember(
        currentItems,
        containerWidthPx,
        itemSizePx,
        itemSpacingPx
    ) {
        buildLayoutInfo(
            items = currentItems,
            containerWidthPx = containerWidthPx,
            itemSizePx = itemSizePx,
            itemSpacingPx = itemSpacingPx,
        )
    }

    val targetItems = remember(
        currentItems,
        rightIndex
    ) {
        if (rightIndex == null) {
            currentItems
        } else {
            currentItems.filterNot { it.index == rightIndex }
        }
    }

    val targetLayout = remember(
        targetItems,
        containerWidthPx,
        itemSizePx,
        itemSpacingPx
    ) {
        buildLayoutInfo(
            items = targetItems,
            containerWidthPx = containerWidthPx,
            itemSizePx = itemSizePx,
            itemSpacingPx = itemSpacingPx,
        )
    }

    val overlayReady =
        moveToAnimate != null && leftIndex != null && rightIndex != null && containerWidthPx > 0 && currentLayout.positions[leftIndex] != null && currentLayout.positions[rightIndex] != null && targetLayout.positions[leftIndex] != null

    LaunchedEffect(
        moveToAnimate,
        overlayReady
    ) {
        if (!overlayReady) return@LaunchedEffect

        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 320,
                easing = FastOutSlowInEasing,
            ),
        )
        onMoveAnimationFinished()
        animationProgress.snapTo(0f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                containerWidthPx = it.size.width
            },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            currentLayout.rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        itemSpacing,
                        Alignment.CenterHorizontally,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    row.forEach { item ->
                        val isSelected = !overlayReady && firstSelectedIndex == item.index

                        val isNeighbour =
                            !overlayReady && firstSelectedIndex != null && kotlin.math.abs(firstSelectedIndex - item.index) == 1

                        Box(
                            modifier = Modifier
                                .size(itemSize)
                                .alpha(if (overlayReady) 0f else 1f)
                                .numberItemModifier(
                                    isSelected = isSelected,
                                    isNeighbour = isNeighbour,
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

        if (overlayReady) {
            val progress = animationProgress.value

            currentItems.forEach { item ->
                val start = currentLayout.positions[item.index] ?: return@forEach

                val end = when (item.index) {
                    rightIndex -> targetLayout.positions[leftIndex]
                    else -> targetLayout.positions[item.index]
                } ?: return@forEach

                val startRow = currentLayout.rowByIndex[item.index] ?: return@forEach
                val endRow = when (item.index) {
                    rightIndex -> targetLayout.rowByIndex[leftIndex]
                    else -> targetLayout.rowByIndex[item.index]
                } ?: return@forEach

                val isMergeTile = item.index == leftIndex || item.index == rightIndex
                val sameRow = startRow == endRow

                val exitLeft = Offset(
                    x = (currentLayout.rowStartX[startRow] ?: start.x) - itemSizePx - itemSpacingPx,
                    y = start.y,
                )

                val enterRight = Offset(
                    x = (targetLayout.rowEndX[endRow] ?: end.x) + itemSizePx + itemSpacingPx,
                    y = end.y,
                )

                if (sameRow) {
                    SingleOverlayCell(
                        value = item.value,
                        position = lerp(
                            start,
                            end,
                            progress
                        ),
                        itemSize = itemSize,
                        isHighlighted = isMergeTile,
                    )
                } else {
                    WrappedOverlayCell(
                        value = item.value,
                        exitPosition = lerp(
                            start,
                            exitLeft,
                            progress
                        ),
                        enterPosition = lerp(
                            enterRight,
                            end,
                            progress
                        ),
                        exitAlpha = 1f - progress,
                        enterAlpha = progress,
                        itemSize = itemSize,
                        isHighlighted = isMergeTile,
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleOverlayCell(
    value: Int,
    position: Offset,
    itemSize: Dp,
    isHighlighted: Boolean,
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = position.x.roundToInt(),
                    y = position.y.roundToInt(),
                )
            }
            .zIndex(1f)
            .size(itemSize)
            .numberItemModifier(
                isSelected = isHighlighted,
                isNeighbour = false,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            color = Color.Black,
        )
    }
}

@Composable
private fun WrappedOverlayCell(
    value: Int,
    exitPosition: Offset,
    enterPosition: Offset,
    exitAlpha: Float,
    enterAlpha: Float,
    itemSize: Dp,
    isHighlighted: Boolean,
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = exitPosition.x.roundToInt(),
                    y = exitPosition.y.roundToInt(),
                )
            }
            .alpha(
                exitAlpha.coerceIn(
                    0f,
                    1f
                )
            )
            .zIndex(1f)
            .size(itemSize)
            .numberItemModifier(
                isSelected = isHighlighted,
                isNeighbour = false,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            color = Color.Black,
        )
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = enterPosition.x.roundToInt(),
                    y = enterPosition.y.roundToInt(),
                )
            }
            .alpha(
                enterAlpha.coerceIn(
                    0f,
                    1f
                )
            )
            .zIndex(1f)
            .size(itemSize)
            .numberItemModifier(
                isSelected = isHighlighted,
                isNeighbour = false,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            color = Color.Black,
        )
    }
}

private data class IndexedNumber(
    val index: Int,
    val value: Int,
)

private data class LayoutInfo(
    val rows: List<List<IndexedNumber>>,
    val positions: Map<Int, Offset>,
    val rowByIndex: Map<Int, Int>,
    val rowStartX: Map<Int, Float>,
    val rowEndX: Map<Int, Float>,
)

private fun buildLayoutInfo(
    items: List<IndexedNumber>,
    containerWidthPx: Int,
    itemSizePx: Float,
    itemSpacingPx: Float,
): LayoutInfo {
    if (items.isEmpty() || containerWidthPx <= 0) {
        return LayoutInfo(
            rows = emptyList(),
            positions = emptyMap(),
            rowByIndex = emptyMap(),
            rowStartX = emptyMap(),
            rowEndX = emptyMap(),
        )
    }

    val maxItems = maxOf(
        1,
        ((containerWidthPx + itemSpacingPx) / (itemSizePx + itemSpacingPx)).toInt(),
    )

    val rows = items.chunked(maxItems)

    val positions = mutableMapOf<Int, Offset>()
    val rowByIndex = mutableMapOf<Int, Int>()
    val rowStartX = mutableMapOf<Int, Float>()
    val rowEndX = mutableMapOf<Int, Float>()

    rows.forEachIndexed { rowIndex, row ->
        val rowWidth = row.size * itemSizePx + (row.size - 1).coerceAtLeast(0) * itemSpacingPx
        val startX = (containerWidthPx - rowWidth) / 2f
        val y = rowIndex * (itemSizePx + itemSpacingPx)

        rowStartX[rowIndex] = startX
        rowEndX[rowIndex] = startX + (row.lastIndex.coerceAtLeast(0)) * (itemSizePx + itemSpacingPx)

        row.forEachIndexed { columnIndex, item ->
            val x = startX + columnIndex * (itemSizePx + itemSpacingPx)
            positions[item.index] = Offset(
                x,
                y
            )
            rowByIndex[item.index] = rowIndex
        }
    }

    return LayoutInfo(
        rows = rows,
        positions = positions,
        rowByIndex = rowByIndex,
        rowStartX = rowStartX,
        rowEndX = rowEndX,
    )
}

private fun Modifier.numberItemModifier(
    isSelected: Boolean,
    isNeighbour: Boolean,
): Modifier {
    val border = when {
        isSelected -> Color(0xFF1565C0)
        isNeighbour -> Color(0xFF42A5F5)
        else -> Color(0xFFBDBDBD)
    }

    val background = when {
        isSelected -> Color(0xFFBBDEFB)
        isNeighbour -> Color(0xFFE3F2FD)
        else -> Color(0xFFF5F5F5)
    }

    return this
        .background(
            color = background,
            shape = RoundedCornerShape(12.dp),
        )
        .border(
            width = 2.dp,
            color = border,
            shape = RoundedCornerShape(12.dp),
        )
}

private fun lerp(
    start: Offset,
    end: Offset,
    fraction: Float
): Offset {
    return Offset(
        x = start.x + (end.x - start.x) * fraction,
        y = start.y + (end.y - start.y) * fraction,
    )
}
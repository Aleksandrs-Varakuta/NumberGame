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
import androidx.compose.runtime.rememberUpdatedState
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
import kotlin.math.abs
import kotlin.math.roundToInt

private const val MOVE_ANIMATION_DURATION_MS = 320

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
    val metrics = remember(
        density,
        itemSize,
        itemSpacing
    ) {
        RowMetrics(
            itemSize = itemSize,
            itemSpacing = itemSpacing,
            itemSizePx = with(density) { itemSize.toPx() },
            itemSpacingPx = with(density) { itemSpacing.toPx() },
        )
    }

    var containerWidthPx by remember { mutableIntStateOf(0) }
    val animationProgress = remember { Animatable(0f) }
    val latestOnMoveAnimationFinished by rememberUpdatedState(onMoveAnimationFinished)

    val items = remember(numbers) { numbers.toIndexedNumbers() }

    val currentLayout = remember(
        items,
        containerWidthPx,
        metrics
    ) {
        buildLayoutInfo(
            items = items,
            containerWidthPx = containerWidthPx,
            itemSizePx = metrics.itemSizePx,
            itemSpacingPx = metrics.itemSpacingPx,
        )
    }

    val animationPlan = remember(
        items,
        moveToAnimate,
        containerWidthPx,
        metrics
    ) {
        buildAnimationPlan(
            items = items,
            move = moveToAnimate,
            containerWidthPx = containerWidthPx,
            itemSizePx = metrics.itemSizePx,
            itemSpacingPx = metrics.itemSpacingPx,
        )
    }

    val shouldFinishInvalidMove = remember(
        moveToAnimate,
        containerWidthPx,
        animationPlan
    ) {
        moveToAnimate != null && containerWidthPx > 0 && animationPlan == null
    }

    LaunchedEffect(shouldFinishInvalidMove) {
        if (shouldFinishInvalidMove) {
            latestOnMoveAnimationFinished()
        }
    }

    LaunchedEffect(animationPlan?.animationKey) {
        if (animationPlan == null) return@LaunchedEffect

        try {
            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = MOVE_ANIMATION_DURATION_MS,
                    easing = FastOutSlowInEasing,
                ),
            )
        } finally {
            animationProgress.snapTo(0f)
            latestOnMoveAnimationFinished()
        }
    }

    val isAnimating = animationPlan != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                containerWidthPx = coordinates.size.width
            },
    ) {
        NumberGrid(
            rows = currentLayout.rows,
            firstSelectedIndex = firstSelectedIndex,
            itemSize = metrics.itemSize,
            itemSpacing = metrics.itemSpacing,
            isVisible = !isAnimating,
            isInteractionEnabled = isInteractionEnabled && !isAnimating,
            onNumberClick = onNumberClick,
        )

        if (animationPlan != null) {
            AnimatedOverlay(
                plan = animationPlan,
                progress = animationProgress.value,
                itemSize = metrics.itemSize,
            )
        }
    }
}

@Composable
private fun NumberGrid(
    rows: List<List<IndexedNumber>>,
    firstSelectedIndex: Int?,
    itemSize: Dp,
    itemSpacing: Dp,
    isVisible: Boolean,
    isInteractionEnabled: Boolean,
    onNumberClick: (Int) -> Unit,
) {
    val alpha = if (isVisible) 1f else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    itemSpacing,
                    Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                row.forEach { item ->
                    NumberCell(
                        value = item.value,
                        itemSize = itemSize,
                        highlightState = resolveHighlightState(
                            firstSelectedIndex = firstSelectedIndex,
                            currentIndex = item.index,
                        ),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            enabled = isInteractionEnabled,
                        ) {
                            onNumberClick(item.index)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedOverlay(
    plan: AnimationPlan,
    progress: Float,
    itemSize: Dp,
) {
    plan.transitions.forEach { transition ->
        when (transition) {
            is CellTransition.Direct -> {
                OverlayCell(
                    value = transition.value,
                    position = lerp(
                        transition.startPosition,
                        transition.endPosition,
                        progress
                    ),
                    itemSize = itemSize,
                    isHighlighted = transition.isHighlighted,
                    alpha = 1f,
                )
            }

            is CellTransition.Wrapped -> {
                OverlayCell(
                    value = transition.value,
                    position = lerp(
                        transition.startPosition,
                        transition.exitPosition,
                        progress
                    ),
                    itemSize = itemSize,
                    isHighlighted = transition.isHighlighted,
                    alpha = 1f - progress,
                )
                OverlayCell(
                    value = transition.value,
                    position = lerp(
                        transition.enterPosition,
                        transition.endPosition,
                        progress
                    ),
                    itemSize = itemSize,
                    isHighlighted = transition.isHighlighted,
                    alpha = progress,
                )
            }
        }
    }
}

@Composable
private fun OverlayCell(
    value: Int,
    position: Offset,
    itemSize: Dp,
    isHighlighted: Boolean,
    alpha: Float,
) {
    NumberCell(
        value = value,
        itemSize = itemSize,
        highlightState = if (isHighlighted) HighlightState.Selected else HighlightState.Normal,
        modifier = Modifier
            .offset {
                IntOffset(
                    x = position.x.roundToInt(),
                    y = position.y.roundToInt(),
                )
            }
            .alpha(
                alpha.coerceIn(
                    0f,
                    1f
                )
            )
            .zIndex(1f),
    )
}

@Composable
private fun NumberCell(
    value: Int,
    itemSize: Dp,
    highlightState: HighlightState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(itemSize)
            .numberItemModifier(highlightState),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value.toString(),
            color = Color.Black,
        )
    }
}

private fun resolveHighlightState(
    firstSelectedIndex: Int?,
    currentIndex: Int,
): HighlightState {
    return when {
        firstSelectedIndex == currentIndex -> HighlightState.Selected
        firstSelectedIndex != null && abs(firstSelectedIndex - currentIndex) == 1 -> HighlightState.Neighbour
        else -> HighlightState.Normal
    }
}

private fun List<Int>.toIndexedNumbers(): List<IndexedNumber> {
    return mapIndexed { index, value ->
        IndexedNumber(
            index = index,
            value = value,
        )
    }
}

private fun Move.canBeAnimated(itemCount: Int): Boolean {
    val startIndex = leftIndex
    val nextIndex = startIndex + 1
    return startIndex in 0 until itemCount && nextIndex in 0 until itemCount
}

private fun buildAnimationPlan(
    items: List<IndexedNumber>,
    move: Move?,
    containerWidthPx: Int,
    itemSizePx: Float,
    itemSpacingPx: Float,
): AnimationPlan? {
    if (move == null || containerWidthPx <= 0 || !move.canBeAnimated(items.size)) {
        return null
    }

    val leftIndex = move.leftIndex
    val rightIndex = leftIndex + 1

    val startLayout = buildLayoutInfo(
        items = items,
        containerWidthPx = containerWidthPx,
        itemSizePx = itemSizePx,
        itemSpacingPx = itemSpacingPx,
    )

    val endItems = items.filterNot { indexedNumber ->
        indexedNumber.index == rightIndex
    }

    val endLayout = buildLayoutInfo(
        items = endItems,
        containerWidthPx = containerWidthPx,
        itemSizePx = itemSizePx,
        itemSpacingPx = itemSpacingPx,
    )

    val transitions = items.mapNotNull { item ->
        val startPosition = startLayout.positions[item.index] ?: return@mapNotNull null
        val targetIndex = if (item.index == rightIndex) leftIndex else item.index
        val endPosition = endLayout.positions[targetIndex] ?: return@mapNotNull null
        val startRow = startLayout.rowByIndex[item.index] ?: return@mapNotNull null
        val endRow = endLayout.rowByIndex[targetIndex] ?: return@mapNotNull null
        val isHighlighted = item.index == leftIndex || item.index == rightIndex

        if (startRow == endRow) {
            CellTransition.Direct(
                value = item.value,
                startPosition = startPosition,
                endPosition = endPosition,
                isHighlighted = isHighlighted,
            )
        } else {
            CellTransition.Wrapped(
                value = item.value,
                startPosition = startPosition,
                exitPosition = Offset(
                    x = (startLayout.rowStartX[startRow]
                        ?: startPosition.x) - itemSizePx - itemSpacingPx,
                    y = startPosition.y,
                ),
                enterPosition = Offset(
                    x = (endLayout.rowEndX[endRow] ?: endPosition.x) + itemSizePx + itemSpacingPx,
                    y = endPosition.y,
                ),
                endPosition = endPosition,
                isHighlighted = isHighlighted,
            )
        }
    }

    if (transitions.size != items.size) {
        return null
    }

    return AnimationPlan(
        animationKey = "${move.leftIndex}:${items.size}:${containerWidthPx}",
        transitions = transitions,
    )
}

private data class RowMetrics(
    val itemSize: Dp,
    val itemSpacing: Dp,
    val itemSizePx: Float,
    val itemSpacingPx: Float,
)

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

private data class AnimationPlan(
    val animationKey: String,
    val transitions: List<CellTransition>,
)

private sealed interface CellTransition {
    val value: Int
    val isHighlighted: Boolean

    data class Direct(
        override val value: Int,
        val startPosition: Offset,
        val endPosition: Offset,
        override val isHighlighted: Boolean,
    ) : CellTransition

    data class Wrapped(
        override val value: Int,
        val startPosition: Offset,
        val exitPosition: Offset,
        val enterPosition: Offset,
        val endPosition: Offset,
        override val isHighlighted: Boolean,
    ) : CellTransition
}

private enum class HighlightState {
    Normal, Selected, Neighbour,
}

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

    val maxItemsInRow = maxOf(
        1,
        ((containerWidthPx + itemSpacingPx) / (itemSizePx + itemSpacingPx)).toInt(),
    )

    val rows = items.chunked(maxItemsInRow)
    val positions = mutableMapOf<Int, Offset>()
    val rowByIndex = mutableMapOf<Int, Int>()
    val rowStartX = mutableMapOf<Int, Float>()
    val rowEndX = mutableMapOf<Int, Float>()

    rows.forEachIndexed { rowIndex, rowItems ->
        val rowWidth =
            rowItems.size * itemSizePx + (rowItems.size - 1).coerceAtLeast(0) * itemSpacingPx
        val startX = (containerWidthPx - rowWidth) / 2f
        val y = rowIndex * (itemSizePx + itemSpacingPx)

        rowStartX[rowIndex] = startX
        rowEndX[rowIndex] =
            startX + rowItems.lastIndex.coerceAtLeast(0) * (itemSizePx + itemSpacingPx)

        rowItems.forEachIndexed { columnIndex, item ->
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
    highlightState: HighlightState,
): Modifier {
    val borderColor = when (highlightState) {
        HighlightState.Selected -> Color(0xFF1565C0)
        HighlightState.Neighbour -> Color(0xFF42A5F5)
        HighlightState.Normal -> Color(0xFFBDBDBD)
    }

    val backgroundColor = when (highlightState) {
        HighlightState.Selected -> Color(0xFFBBDEFB)
        HighlightState.Neighbour -> Color(0xFFE3F2FD)
        HighlightState.Normal -> Color(0xFFF5F5F5)
    }

    return background(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
    ).border(
        width = 2.dp,
        color = borderColor,
        shape = RoundedCornerShape(12.dp),
    )
}

private fun lerp(
    start: Offset,
    end: Offset,
    fraction: Float,
): Offset {
    return Offset(
        x = start.x + (end.x - start.x) * fraction,
        y = start.y + (end.y - start.y) * fraction,
    )
}

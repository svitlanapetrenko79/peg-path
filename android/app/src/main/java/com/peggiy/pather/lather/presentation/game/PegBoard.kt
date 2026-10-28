package com.peggiy.pather.lather.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassFill
import com.peggiy.pather.lather.core.theme.HotPink
import com.peggiy.pather.lather.core.ui.drawGlossyPeg
import com.peggiy.pather.lather.core.ui.tokenDrawable
import kotlin.math.roundToInt

private val TOKEN_SIZE = 30.dp
private val TRAIL_SIZE = 10.dp

@Composable
fun PegBoard(
    state: GameUiState,
    slotWidth: Dp,
    rowPitch: Dp,
    modifier: Modifier = Modifier
) {
    val innerWidth = slotWidth * GameConfig.SLOT_COUNT
    val topOffset = GameConfig.BOARD_TOP_OFFSET_DP.dp
    val innerHeight = topOffset + rowPitch * GameConfig.PEG_ROWS +
        GameConfig.SLOT_STRIP_DP.dp + 10.dp

    Box(
        modifier = modifier
            .width(innerWidth + (GameConfig.BOARD_PAD_DP + GameConfig.BOARD_BORDER_DP).dp * 2)
            .height(innerHeight + (GameConfig.BOARD_PAD_DP + GameConfig.BOARD_BORDER_DP).dp * 2)
            .clip(RoundedCornerShape(24.dp))
            .background(GlassFill)
            .border(
                border = BorderStroke(
                    GameConfig.BOARD_BORDER_DP.dp,
                    Brush.verticalGradient(listOf(Color(0x5900D4FF), Color(0x598B00FF)))
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(GameConfig.BOARD_PAD_DP.dp + GameConfig.BOARD_BORDER_DP.dp)
    ) {
        Box(
            modifier = Modifier
                .width(innerWidth)
                .height(innerHeight)
                .align(Alignment.TopCenter)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pitchPx = rowPitch.toPx()
                val topPx = topOffset.toPx()
                val slotPx = slotWidth.toPx()
                for (row in 0 until GameConfig.PEG_ROWS) {
                    val count = row + 3
                    val y = topPx + row * pitchPx
                    val startX = size.width / 2f - (count - 1) * slotPx / 2f
                    val struck = row == state.hitRow
                    for (index in 0 until count) {
                        drawGlossyPeg(
                            center = Offset(startX + index * slotPx, y),
                            radius = 4.5.dp.toPx(),
                            color = if (struck) HotPink else Cyan,
                            alpha = if (struck) 1f else 0.85f,
                            highlightAlpha = 0.6f
                        )
                    }
                }
            }

            if (state.tokenVisible && !state.reducedMotion) {
                for (step in 1..6) {
                    TokenTrail(
                        state = state,
                        slotWidth = slotWidth,
                        rowPitch = rowPitch,
                        topOffset = topOffset,
                        step = step
                    )
                }
            }

            if (state.tokenVisible) {
                Image(
                    painter = painterResource(tokenDrawable(state.selectedToken)),
                    contentDescription = stringResource(R.string.cd_token),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(TOKEN_SIZE)
                        .offset {
                            tokenOffset(
                                column = state.tokenColumn,
                                row = state.tokenRow,
                                slotPx = slotWidth.toPx(),
                                pitchPx = rowPitch.toPx(),
                                topPx = topOffset.toPx(),
                                sizePx = TOKEN_SIZE.toPx()
                            )
                        }
                )
            }

            SlotStrip(
                slotWidth = slotWidth,
                activeSlot = state.activeSlot,
                landedSlots = state.landedSlots,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TokenTrail(
    state: GameUiState,
    slotWidth: Dp,
    rowPitch: Dp,
    topOffset: Dp,
    step: Int
) {
    val lag = step * 0.055f
    val row = (state.tokenRow - lag * GameConfig.PEG_ROWS).coerceAtLeast(0f)
    Box(
        modifier = Modifier
            .size(TRAIL_SIZE)
            .offset {
                tokenOffset(
                    column = state.tokenColumn,
                    row = row,
                    slotPx = slotWidth.toPx(),
                    pitchPx = rowPitch.toPx(),
                    topPx = topOffset.toPx(),
                    sizePx = TRAIL_SIZE.toPx()
                )
            }
            .height(TRAIL_SIZE)
            .clip(RoundedCornerShape(50))
            .border(
                border = BorderStroke(1.dp, Cyan.copy(alpha = 0.5f - step * 0.07f)),
                shape = RoundedCornerShape(50)
            )
    )
}

private fun tokenOffset(
    column: Float,
    row: Float,
    slotPx: Float,
    pitchPx: Float,
    topPx: Float,
    sizePx: Float
): IntOffset {
    val centerX = (column + 0.5f) * slotPx
    val centerY = topPx + row * pitchPx
    return IntOffset(
        (centerX - sizePx / 2f).roundToInt(),
        (centerY - sizePx / 2f).roundToInt()
    )
}

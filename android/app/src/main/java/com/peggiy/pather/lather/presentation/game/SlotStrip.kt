package com.peggiy.pather.lather.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.core.theme.BgBase
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.HotPink
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TrackInactive
import com.peggiy.pather.lather.core.theme.Violet
import com.peggiy.pather.lather.domain.usecase.SparksFormatter

fun slotColor(multiplier: Float): Color = when {
    multiplier >= 6.0f -> HotPink
    multiplier >= 1.6f -> Violet
    multiplier >= 1.0f -> Cyan
    else -> TrackInactive
}

@Composable
fun SlotStrip(
    slotWidth: Dp,
    activeSlot: Int,
    landedSlots: List<Int>,
    modifier: Modifier = Modifier
) {
    val pop by animateFloatAsState(
        targetValue = if (activeSlot >= 0) 1.18f else 1f,
        animationSpec = tween(durationMillis = 340),
        label = "slot_pop"
    )
    Row(modifier = modifier.fillMaxWidth().height(44.dp)) {
        GameConfig.MULTIPLIERS.forEachIndexed { index, multiplier ->
            val base = slotColor(multiplier)
            val landed = landedSlots.contains(index)
            val active = index == activeSlot
            val fill = when {
                active -> base
                landed -> base.copy(alpha = 0.55f)
                else -> base.copy(alpha = 0.26f)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(slotWidth)
                    .fillMaxHeight()
                    .padding(horizontal = 1.dp, vertical = 2.dp)
                    .graphicsLayer {
                        if (active) {
                            scaleY = pop
                        }
                    }
                    .background(color = fill, shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = stringResource(
                        R.string.mult_fmt,
                        SparksFormatter.multiplier(multiplier)
                    ),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        letterSpacing = 0.sp
                    ),
                    color = if (active || landed) BgBase else TextMuted,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

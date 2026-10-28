package com.peggiy.pather.lather.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextSecondary

@Composable
fun BetChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    stateLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fill = when {
        !enabled -> Color(0x14120026)
        selected -> Color(0x3300D4FF)
        else -> Color(0x33120026)
    }
    val border = when {
        !enabled -> Color.Transparent
        selected -> Cyan
        else -> GlassBorder
    }
    val labelColor = when {
        !enabled -> TextMuted.copy(alpha = 0.4f)
        selected -> Cyan
        else -> TextSecondary
    }
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        color = fill,
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, border),
        tonalElevation = 0.dp,
        modifier = modifier
            .height(48.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = labelColor,
                maxLines = 1
            )
        }
    }
}

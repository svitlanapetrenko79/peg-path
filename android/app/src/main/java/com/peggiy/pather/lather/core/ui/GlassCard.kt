package com.peggiy.pather.lather.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.GlassFill

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    fill: Color = GlassFill,
    borderColor: Color = GlassBorder,
    corner: Dp = 24.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(corner),
        color = fill,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(contentPadding),
            content = content
        )
    }
}

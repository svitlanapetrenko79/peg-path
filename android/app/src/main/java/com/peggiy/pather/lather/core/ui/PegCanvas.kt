package com.peggiy.pather.lather.core.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawGlossyPeg(
    center: Offset,
    radius: Float,
    color: Color,
    alpha: Float = 0.85f,
    highlightAlpha: Float = 0.6f
) {
    drawCircle(color = color.copy(alpha = alpha), radius = radius, center = center)
    drawCircle(
        color = Color.White.copy(alpha = highlightAlpha),
        radius = radius * 0.42f,
        center = Offset(center.x - radius * 0.32f, center.y - radius * 0.32f)
    )
}

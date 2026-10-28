package com.peggiy.pather.lather.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.theme.BgBase
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.Violet

@Composable
fun ScreenBackground(
    overlay: Brush,
    modifier: Modifier = Modifier,
    blobDrift: Dp = 0.dp,
    violetAlpha: Float = 0.18f,
    cyanAlpha: Float = 0.14f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(BgBase)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(overlay))
        Canvas(modifier = Modifier.fillMaxSize()) {
            val drift = blobDrift.toPx()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Violet.copy(alpha = violetAlpha), Color.Transparent),
                    center = Offset(size.width * 0.28f + drift, size.height * 0.34f - drift),
                    radius = size.minDimension * 0.62f
                ),
                radius = size.minDimension * 0.62f,
                center = Offset(size.width * 0.28f + drift, size.height * 0.34f - drift)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Cyan.copy(alpha = cyanAlpha), Color.Transparent),
                    center = Offset(size.width * 0.74f - drift, size.height * 0.62f + drift),
                    radius = size.minDimension * 0.52f
                ),
                radius = size.minDimension * 0.52f,
                center = Offset(size.width * 0.74f - drift, size.height * 0.62f + drift)
            )
        }
        content()
    }
}

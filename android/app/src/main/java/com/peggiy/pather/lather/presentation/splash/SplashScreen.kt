package com.peggiy.pather.lather.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.di.AppViewModelFactory
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassBorderCyan
import com.peggiy.pather.lather.core.theme.GlassHighlight
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary
import com.peggiy.pather.lather.core.theme.Violet
import com.peggiy.pather.lather.core.ui.ScreenBackground
import com.peggiy.pather.lather.core.ui.drawGlossyPeg

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    var handedOff by remember { mutableStateOf(false) }

    LaunchedEffect(finished) {
        if (finished && !handedOff) {
            handedOff = true
            onFinished()
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }

    val drift = rememberInfiniteTransition(label = "splash_aurora")
    val blobShift by drift.animateFloat(
        initialValue = -14f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob_shift"
    )
    val ringPulse by drift.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_pulse"
    )
    val pegBob by drift.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "peg_bob"
    )

    val logoScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.78f,
        animationSpec = tween(durationMillis = 520),
        label = "logo_scale"
    )
    val logoAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 420),
        label = "logo_alpha"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 420, delayMillis = 180),
        label = "title_alpha"
    )
    val taglineAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 420, delayMillis = 320),
        label = "tagline_alpha"
    )

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(Color(0xF20A0014), Color(0xE61A0038), Color(0xF50A0014))
        ),
        blobDrift = blobShift.dp,
        violetAlpha = 0.18f,
        cyanAlpha = 0.14f
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = GlassHighlight,
                border = BorderStroke(1.dp, GlassBorderCyan),
                tonalElevation = 0.dp,
                modifier = Modifier
                    .size(196.dp)
                    .graphicsLayer {
                        scaleX = ringPulse
                        scaleY = ringPulse
                    }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(164.dp)
                            .graphicsLayer {
                                scaleX = logoScale
                                scaleY = logoScale
                                alpha = logoAlpha
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.brand),
                    style = MaterialTheme.typography.displayLarge,
                    color = Violet.copy(alpha = 0.45f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .graphicsLayer { alpha = titleAlpha }
                )
                Text(
                    text = stringResource(R.string.brand),
                    style = MaterialTheme.typography.displayLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer { alpha = titleAlpha }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = taglineAlpha }
            )

            Spacer(modifier = Modifier.height(28.dp))

            LinearProgressIndicator(
                color = Cyan,
                trackColor = Color(0x3300D4FF),
                strokeCap = StrokeCap.Round,
                modifier = Modifier.width(196.dp).height(4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(44.dp))

            Canvas(modifier = Modifier.fillMaxWidth().height(28.dp)) {
                val count = 6
                val gap = size.width / (count + 1)
                for (index in 0 until count) {
                    val phase = if (index % 2 == 0) pegBob else -pegBob
                    drawGlossyPeg(
                        center = Offset(gap * (index + 1), size.height / 2f + phase),
                        radius = 7.dp.toPx() / 2f,
                        color = Cyan,
                        alpha = 0.8f * titleAlpha,
                        highlightAlpha = 0.55f * titleAlpha
                    )
                }
            }
        }
    }
}

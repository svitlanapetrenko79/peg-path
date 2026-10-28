package com.peggiy.pather.lather.presentation.gameover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.di.AppViewModelFactory
import com.peggiy.pather.lather.core.navigation.Screen
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassBorderCyan
import com.peggiy.pather.lather.core.theme.GlassHighlight
import com.peggiy.pather.lather.core.theme.HotPink
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.Violet
import com.peggiy.pather.lather.core.ui.GlassCard
import com.peggiy.pather.lather.core.ui.PrimaryButton
import com.peggiy.pather.lather.core.ui.ScreenBackground
import com.peggiy.pather.lather.core.ui.SecondaryButton
import com.peggiy.pather.lather.core.ui.StatCard
import com.peggiy.pather.lather.core.ui.rewardDrawable

@Composable
fun GameOverRoute(
    result: Screen.GameOver,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        gameOverViewModel.refresh()
    }

    GameOverScreen(
        state = state,
        newBest = result.newBest || state.newBest,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverScreen(
    state: GameOverUiState,
    newBest: Boolean,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val liveScore = stringResource(R.string.score_live_fmt, state.roundScoreText)

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(Color(0xE61A0038), Color(0x4D8B00FF), Color(0xF20A0014))
        ),
        violetAlpha = 0.26f,
        cyanAlpha = 0.10f
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.6f))

            Surface(
                shape = CircleShape,
                color = GlassHighlight,
                border = BorderStroke(1.dp, GlassBorderCyan),
                tonalElevation = 0.dp,
                modifier = Modifier.size(108.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(76.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.round_complete),
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 30.sp),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(
                visible = newBest && entered,
                enter = scaleIn(
                    initialScale = 0.9f,
                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
                ) + fadeIn(animationSpec = tween(280))
            ) {
                Text(
                    text = stringResource(R.string.new_best),
                    style = MaterialTheme.typography.labelLarge,
                    color = HotPink,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            GlassCard(
                borderColor = GlassBorderCyan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.roundScoreText,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 46.sp,
                        fontWeight = FontWeight.Black
                    ),
                    color = Cyan,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                            contentDescription = liveScore
                        }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.sparks_won_fmt, state.sparksWonText),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(
                    label = stringResource(R.string.stat_best_x),
                    value = stringResource(R.string.mult_fmt, state.bestMultiplierText),
                    accent = HotPink,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.stat_drops),
                    value = state.dropsUsed.coerceAtLeast(1).toString(),
                    accent = Cyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = stringResource(R.string.stat_balance),
                    value = state.balanceText,
                    accent = Violet,
                    modifier = Modifier.weight(1f)
                )
            }

            if (state.rewards.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.rewards_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.rewards.forEach { reward ->
                        RewardChip(reward = reward)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.drop_again),
                onClick = onPlayAgain,
                icon = painterResource(R.drawable.ic_refresh),
                height = 60.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconPainter = painterResource(R.drawable.icon_home),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RewardChip(reward: Int) {
    val labelRes = when (reward) {
        0 -> R.string.reward_xp
        1 -> R.string.reward_token
        else -> R.string.reward_challenge
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0x59120026),
        border = BorderStroke(1.dp, GlassBorderCyan),
        tonalElevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Image(
                painter = painterResource(rewardDrawable(reward)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

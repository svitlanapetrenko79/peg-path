package com.peggiy.pather.lather.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.core.di.AppViewModelFactory
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.GlassFillStrong
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary
import com.peggiy.pather.lather.core.theme.TrackInactive
import com.peggiy.pather.lather.core.ui.BetChip
import com.peggiy.pather.lather.core.ui.PrimaryButton
import com.peggiy.pather.lather.core.ui.ScreenBackground
import com.peggiy.pather.lather.core.ui.SparksBadge
import com.peggiy.pather.lather.domain.model.Phase
import com.peggiy.pather.lather.presentation.dialog.PauseDialog
import com.peggiy.pather.lather.presentation.dialog.QuitDialog
import kotlin.math.floor

@Composable
fun GameRoute(
    roundToken: Int,
    onLeave: () -> Unit,
    onRoundFinished: (Int, Int, Boolean) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(roundToken) {
        gameViewModel.newRound(roundToken)
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            onRoundFinished(event.roundScore, event.bestSlot, event.newBest)
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameScreen(
        state = state,
        onDrop = gameViewModel::drop,
        onSelectBet = gameViewModel::selectBet,
        onPause = gameViewModel::openPause,
        onResume = gameViewModel::closePause,
        onAskQuit = gameViewModel::openQuit,
        onCancelQuit = gameViewModel::closeQuit,
        onQuit = onLeave
    )
}

@Composable
private fun GameScreen(
    state: GameUiState,
    onDrop: () -> Unit,
    onSelectBet: (Int) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onAskQuit: () -> Unit,
    onCancelQuit: () -> Unit,
    onQuit: () -> Unit
) {
    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(Color(0xCC0A0014), Color(0x990D1B2A), Color(0xE60A0014))
        ),
        violetAlpha = 0.14f,
        cyanAlpha = 0.18f
    ) {
        Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            GameTopBar(state = state, onHome = onAskQuit, onPause = onPause)
            GameScoreStrip(state = state)
            BoardArea(state = state, modifier = Modifier.weight(1f).fillMaxWidth())
            Spacer(modifier = Modifier.height(166.dp))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .safeDrawingPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = GlassFillStrong,
                border = BorderStroke(1.dp, GlassBorder),
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    PrimaryButton(
                        text = if (state.phase == Phase.FALLING || state.phase == Phase.SETTLING) {
                            stringResource(R.string.dropping)
                        } else {
                            stringResource(R.string.drop)
                        },
                        onClick = onDrop,
                        icon = painterResource(R.drawable.ic_arrow_down),
                        enabled = state.canDrop,
                        height = 56.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = GlassFillStrong,
                border = BorderStroke(1.dp, GlassBorder),
                tonalElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bet_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    GameConfig.BETS.forEach { value ->
                        BetChip(
                            label = value.toString(),
                            selected = state.bet == value,
                            enabled = state.controlsEnabled && state.balance >= value,
                            stateLabel = if (state.bet == value) {
                                stringResource(R.string.chip_selected)
                            } else {
                                stringResource(R.string.chip_not_selected)
                            },
                            onClick = { onSelectBet(value) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    if (state.pauseVisible) {
        PauseDialog(onResume = onResume, onQuit = onAskQuit)
    }
    if (state.quitVisible) {
        QuitDialog(onConfirm = onQuit, onDismiss = onCancelQuit)
    }
}

@Composable
private fun GameTopBar(
    state: GameUiState,
    onHome: () -> Unit,
    onPause: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0x660A0014))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 8.dp)
        ) {
            IconButton(onClick = onHome, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(
                        R.string.drops_fmt,
                        (state.dropsUsed + 1).coerceAtMost(GameConfig.ROUND_DROPS),
                        GameConfig.ROUND_DROPS
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (index in 0 until GameConfig.ROUND_DROPS) {
                        Spacer(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = if (index < state.dropsUsed) Cyan else TrackInactive,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassBorder)
        )
    }
}

@Composable
private fun GameScoreStrip(state: GameUiState) {
    val liveScore = stringResource(R.string.score_live_fmt, state.roundScoreText)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
    ) {
        SparksBadge(
            amount = state.balanceText,
            spokenLabel = stringResource(R.string.balance_live_fmt, state.balanceText)
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = stringResource(R.string.score_label),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Text(
                text = state.roundScoreText,
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = liveScore
                }
            )
        }
    }
}

@Composable
private fun BoardArea(state: GameUiState, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        val frame = (GameConfig.BOARD_PAD_DP + GameConfig.BOARD_BORDER_DP).dp
        val boardMaxWidth = minOf(maxWidth, GameConfig.BOARD_MAX_DP.dp)
        val slotWidth = floor(((boardMaxWidth - frame * 2) / GameConfig.SLOT_COUNT).value).dp
        val fixedHeight = GameConfig.BOARD_TOP_OFFSET_DP.dp +
            GameConfig.SLOT_STRIP_DP.dp + 10.dp + frame * 2
        val available = maxHeight - fixedHeight
        val pitch = minOf(34.dp, maxOf(18.dp, available / GameConfig.PEG_ROWS))
        PegBoard(state = state, slotWidth = slotWidth, rowPitch = pitch)
    }
}

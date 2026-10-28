package com.peggiy.pather.lather.presentation.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.HotPink
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary
import com.peggiy.pather.lather.core.theme.TrackInactive
import com.peggiy.pather.lather.core.theme.Violet
import com.peggiy.pather.lather.core.ui.PrimaryButton
import com.peggiy.pather.lather.core.ui.ScreenBackground
import com.peggiy.pather.lather.core.ui.SecondaryButton
import com.peggiy.pather.lather.core.ui.SparksBadge
import com.peggiy.pather.lather.core.ui.StatCard
import com.peggiy.pather.lather.core.ui.drawGlossyPeg
import com.peggiy.pather.lather.core.ui.tokenDrawable
import com.peggiy.pather.lather.presentation.dialog.DailyBonusDialog
import com.peggiy.pather.lather.presentation.dialog.ResetConfirmDialog
import com.peggiy.pather.lather.presentation.dialog.SettingsDialog
import com.peggiy.pather.lather.presentation.panel.ChallengePanel
import com.peggiy.pather.lather.presentation.panel.CollectionPanel
import com.peggiy.pather.lather.presentation.panel.ProfilePanel

@Composable
fun MenuRoute(onPlay: () -> Unit) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    MenuScreen(
        state = state,
        onPlay = {
            menuViewModel.onPlay()
            onPlay()
        },
        onOpenChallenges = menuViewModel::openChallenges,
        onOpenCollection = menuViewModel::openCollection,
        onOpenProfile = menuViewModel::openProfile,
        onOpenBonus = menuViewModel::openBonus,
        onOpenSettings = menuViewModel::openSettings,
        onDismiss = menuViewModel::dismissPanels,
        onSelectToken = menuViewModel::selectToken,
        onSelectLayout = menuViewModel::selectLayout,
        onClaimChallenge = menuViewModel::claimChallenge,
        onClaimBonus = menuViewModel::claimDailyBonus,
        onSound = menuViewModel::setSound,
        onHaptics = menuViewModel::setHaptics,
        onReducedMotion = menuViewModel::setReducedMotion,
        onAskReset = menuViewModel::openResetConfirm,
        onCancelReset = menuViewModel::dismissResetConfirm,
        onConfirmReset = menuViewModel::resetProgress
    )
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpenChallenges: () -> Unit,
    onOpenCollection: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenBonus: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
    onSelectToken: (Int) -> Unit,
    onSelectLayout: (Int) -> Unit,
    onClaimChallenge: (Int) -> Unit,
    onClaimBonus: () -> Unit,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(Color(0x330A0014), Color(0x66120026), Color(0xF21A0038))
        ),
        violetAlpha = 0.22f,
        cyanAlpha = 0.16f
    ) {
        Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            MenuArtZone(
                state = state,
                entered = entered,
                onOpenProfile = onOpenProfile,
                onOpenSettings = onOpenSettings,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            AnimatedVisibility(
                visible = entered,
                enter = slideInVertically(animationSpec = tween(420)) { it / 3 } +
                    fadeIn(animationSpec = tween(420))
            ) {
                MenuSheet(
                    state = state,
                    onPlay = onPlay,
                    onOpenChallenges = onOpenChallenges,
                    onOpenCollection = onOpenCollection,
                    onOpenBonus = onOpenBonus
                )
            }
        }
    }

    if (state.challengePanelVisible) {
        ChallengePanel(
            challenges = state.challenges,
            layouts = state.layouts,
            onClaim = onClaimChallenge,
            onSelectLayout = onSelectLayout,
            onDismiss = onDismiss
        )
    }
    if (state.collectionPanelVisible) {
        CollectionPanel(
            tokens = state.tokens,
            onSelect = onSelectToken,
            onDismiss = onDismiss
        )
    }
    if (state.profilePanelVisible) {
        ProfilePanel(
            state = state,
            onReset = onAskReset,
            onDismiss = onDismiss
        )
    }
    if (state.bonusDialogVisible) {
        DailyBonusDialog(
            streak = state.bonusStreak,
            claimable = state.bonusClaimable,
            onClaim = onClaimBonus,
            onDismiss = onDismiss
        )
    }
    if (state.settingsDialogVisible) {
        SettingsDialog(
            soundOn = state.soundOn,
            hapticsOn = state.hapticsOn,
            reducedMotion = state.reducedMotion,
            onSound = onSound,
            onHaptics = onHaptics,
            onReducedMotion = onReducedMotion,
            onDismiss = onDismiss
        )
    }
    if (state.resetDialogVisible) {
        ResetConfirmDialog(onConfirm = onConfirmReset, onDismiss = onCancelReset)
    }
}

@Composable
private fun MenuArtZone(
    state: MenuUiState,
    entered: Boolean,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.align(Alignment.TopStart)) {
            IconButton(onClick = onOpenProfile, modifier = Modifier.size(48.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = stringResource(R.string.cd_profile),
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onOpenSettings, modifier = Modifier.size(48.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.cd_settings),
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        SparksBadge(
            amount = state.balanceText,
            spokenLabel = stringResource(R.string.balance_live_fmt, state.balanceText),
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(top = 56.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(108.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Box(contentAlignment = Alignment.TopCenter) {
                Canvas(modifier = Modifier.fillMaxWidth().height(112.dp)) {
                    val pitch = 26.dp.toPx()
                    val rows = listOf(5, 6, 7)
                    rows.forEachIndexed { rowIndex, count ->
                        val y = 34.dp.toPx() + rowIndex * pitch
                        val startX = size.width / 2f - (count - 1) * pitch / 2f
                        for (index in 0 until count) {
                            drawGlossyPeg(
                                center = Offset(startX + index * pitch, y),
                                radius = 5.dp.toPx(),
                                color = Cyan,
                                alpha = 0.55f,
                                highlightAlpha = 0.4f
                            )
                        }
                    }
                }
                Image(
                    painter = painterResource(tokenDrawable(state.selectedToken)),
                    contentDescription = stringResource(R.string.cd_token),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(56.dp)
                )
            }
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpenChallenges: () -> Unit,
    onOpenCollection: () -> Unit,
    onOpenBonus: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = Color(0xE6120026),
        border = BorderStroke(1.dp, GlassBorder),
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(Brush.horizontalGradient(listOf(Cyan, Violet)))
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 16.dp)
            ) {
                Text(
                    text = if (state.welcomeVisible) {
                        stringResource(R.string.welcome_line)
                    } else {
                        stringResource(R.string.teaser_line)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = Cyan,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.brand),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.menu_tagline),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.level_fmt, state.level),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    LinearProgressIndicator(
                        progress = { state.xpFraction },
                        color = Cyan,
                        trackColor = TrackInactive,
                        strokeCap = StrokeCap.Round,
                        gapSize = 0.dp,
                        drawStopIndicator = {},
                        modifier = Modifier.weight(1f).height(6.dp)
                    )
                }
                if (state.statCardsVisible) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.bestScore > 0) {
                            StatCard(
                                label = stringResource(R.string.stat_best),
                                value = state.bestScoreText,
                                accent = Violet,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (state.lifetimeDrops > 0) {
                            StatCard(
                                label = stringResource(R.string.stat_drops),
                                value = state.lifetimeDropsText,
                                accent = Cyan,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (state.bestMultiplier > 0f) {
                            StatCard(
                                label = stringResource(R.string.stat_top_x),
                                value = stringResource(R.string.mult_fmt, state.bestMultiplierText),
                                accent = HotPink,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = stringResource(R.string.menu_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                PrimaryButton(
                    text = stringResource(R.string.play),
                    onClick = onPlay,
                    icon = painterResource(R.drawable.ic_play),
                    height = 60.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SecondaryButton(
                        text = stringResource(R.string.daily_bonus),
                        onClick = onOpenBonus,
                        textStyle = MaterialTheme.typography.labelSmall,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = stringResource(R.string.challenge),
                        onClick = onOpenChallenges,
                        textStyle = MaterialTheme.typography.labelSmall,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        text = stringResource(R.string.collection),
                        onClick = onOpenCollection,
                        textStyle = MaterialTheme.typography.labelSmall,
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.disclaimer),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


package com.peggiy.pather.lather.presentation.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TrackInactive
import com.peggiy.pather.lather.core.ui.GlassCard
import com.peggiy.pather.lather.core.ui.SecondaryButton
import com.peggiy.pather.lather.presentation.menu.MenuUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePanel(
    state: MenuUiState,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xF2120026),
        contentColor = TextPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.panel_profile),
                style = MaterialTheme.typography.labelLarge,
                color = Cyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.level_fmt, state.level),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    LinearProgressIndicator(
                        progress = { state.xpFraction },
                        color = Cyan,
                        trackColor = TrackInactive,
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier.weight(1f).height(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileRow(
                    label = stringResource(R.string.profile_drops),
                    value = state.lifetimeDropsText
                )
                ProfileRow(
                    label = stringResource(R.string.profile_best),
                    value = state.bestScoreText
                )
                ProfileRow(
                    label = stringResource(R.string.profile_top),
                    value = stringResource(R.string.mult_fmt, state.bestMultiplierText)
                )
                ProfileRow(
                    label = stringResource(R.string.profile_tokens),
                    value = state.tokensOwned.toString()
                )
                ProfileRow(
                    label = stringResource(R.string.profile_challenges),
                    value = state.challengesCompleted.toString()
                )
                ProfileRow(
                    label = stringResource(R.string.stat_balance),
                    value = state.balanceText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                text = stringResource(R.string.reset_progress),
                onClick = onReset,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.close),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
    }
}

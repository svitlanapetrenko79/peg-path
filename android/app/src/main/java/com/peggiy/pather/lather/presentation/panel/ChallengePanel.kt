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
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.GlassBorderCyan
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary
import com.peggiy.pather.lather.core.theme.TrackInactive
import com.peggiy.pather.lather.core.ui.GlassCard
import com.peggiy.pather.lather.core.ui.SecondaryButton
import com.peggiy.pather.lather.presentation.menu.ChallengeCardUi
import com.peggiy.pather.lather.presentation.menu.LayoutCardUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengePanel(
    challenges: List<ChallengeCardUi>,
    layouts: List<LayoutCardUi>,
    onClaim: (Int) -> Unit,
    onSelectLayout: (Int) -> Unit,
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
                text = stringResource(R.string.panel_challenges),
                style = MaterialTheme.typography.labelLarge,
                color = Cyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            challenges.forEach { challenge ->
                GlassCard(
                    borderColor = if (challenge.complete) GlassBorderCyan else GlassBorder,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                ) {
                    Text(
                        text = stringResource(challenge.titleRes),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { challenge.fraction },
                            color = Cyan,
                            trackColor = TrackInactive,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.weight(1f).height(6.dp)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                        Text(
                            text = stringResource(
                                R.string.progress_fmt,
                                challenge.current,
                                challenge.target
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        text = if (challenge.claimed) {
                            stringResource(R.string.claimed)
                        } else {
                            stringResource(R.string.claim)
                        },
                        onClick = { onClaim(challenge.id) },
                        enabled = challenge.complete && !challenge.claimed,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.panel_layouts),
                style = MaterialTheme.typography.labelLarge,
                color = Cyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                layouts.forEach { layout ->
                    GlassCard(
                        borderColor = if (layout.active) GlassBorderCyan else GlassBorder,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(layout.nameRes),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (layout.unlocked) TextPrimary else TextMuted
                                )
                                Text(
                                    text = if (layout.unlocked) {
                                        stringResource(R.string.unlocked)
                                    } else {
                                        stringResource(
                                            R.string.locked_level_fmt,
                                            layout.unlockLevel
                                        )
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                            SecondaryButton(
                                text = if (layout.active) {
                                    stringResource(R.string.equipped)
                                } else {
                                    stringResource(R.string.select)
                                },
                                onClick = { onSelectLayout(layout.id) },
                                enabled = layout.unlocked && !layout.active,
                                textStyle = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                text = stringResource(R.string.close),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

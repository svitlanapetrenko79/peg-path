package com.peggiy.pather.lather.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.SurfaceElevated
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary
import com.peggiy.pather.lather.core.theme.TrackInactive

@Composable
fun DailyBonusDialog(
    streak: Int,
    claimable: Boolean,
    onClaim: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = stringResource(R.string.bonus_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (day in 1..GameConfig.BONUS_STREAK_DAYS) {
                        val reached = day <= streak
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (reached) Color(0x3300D4FF) else TrackInactive,
                            border = BorderStroke(
                                1.dp,
                                if (day == streak) Cyan else GlassBorder
                            ),
                            tonalElevation = 0.dp,
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = stringResource(R.string.day_fmt, day),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (reached) Cyan else TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = if (claimable) {
                        stringResource(R.string.bonus_body_fmt, streak)
                    } else {
                        stringResource(R.string.bonus_claimed)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onClaim, enabled = claimable) {
                Text(
                    text = stringResource(R.string.bonus_claim),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (claimable) Cyan else TextMuted
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
            }
        }
    )
}

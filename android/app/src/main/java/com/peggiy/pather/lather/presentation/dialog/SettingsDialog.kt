package com.peggiy.pather.lather.presentation.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.core.theme.BgBase
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.SurfaceElevated
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary
import com.peggiy.pather.lather.core.theme.TrackInactive

@Composable
fun SettingsDialog(
    soundOn: Boolean,
    hapticsOn: Boolean,
    reducedMotion: Boolean,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
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
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SettingsRow(
                    label = stringResource(R.string.settings_sound),
                    checked = soundOn,
                    onChange = onSound
                )
                SettingsRow(
                    label = stringResource(R.string.settings_haptics),
                    checked = hapticsOn,
                    onChange = onHaptics
                )
                SettingsRow(
                    label = stringResource(R.string.settings_reduced),
                    checked = reducedMotion,
                    onChange = onReducedMotion
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.settings_about_fmt, GameConfig.APP_VERSION),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    color = Cyan
                )
            }
        }
    )
}

@Composable
private fun SettingsRow(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    val stateLabel = if (checked) {
        stringResource(R.string.state_on)
    } else {
        stringResource(R.string.state_off)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BgBase,
                checkedTrackColor = Cyan,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = TrackInactive
            ),
            modifier = Modifier.semantics { stateDescription = stateLabel }
        )
    }
}

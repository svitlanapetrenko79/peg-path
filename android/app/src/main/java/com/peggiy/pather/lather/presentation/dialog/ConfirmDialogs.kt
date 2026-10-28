package com.peggiy.pather.lather.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.HotPink
import com.peggiy.pather.lather.core.theme.SurfaceElevated
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.theme.TextSecondary

@Composable
private fun BaseConfirmDialog(
    title: String,
    body: String,
    confirmLabel: String,
    dismissLabel: String,
    confirmColor: Color,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDismissRequest: () -> Unit = onDismiss
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        containerColor = SurfaceElevated,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = body, style = MaterialTheme.typography.bodyLarge)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = confirmColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
            }
        }
    )
}

@Composable
fun ResetConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    BaseConfirmDialog(
        title = stringResource(R.string.reset_title),
        body = stringResource(R.string.reset_body),
        confirmLabel = stringResource(R.string.confirm),
        dismissLabel = stringResource(R.string.cancel),
        confirmColor = HotPink,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
fun QuitDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    BaseConfirmDialog(
        title = stringResource(R.string.quit_title),
        body = stringResource(R.string.quit_body),
        confirmLabel = stringResource(R.string.quit_to_menu),
        dismissLabel = stringResource(R.string.cancel),
        confirmColor = HotPink,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
fun PauseDialog(onResume: () -> Unit, onQuit: () -> Unit) {
    BaseConfirmDialog(
        title = stringResource(R.string.pause_title),
        body = stringResource(R.string.pause_body),
        confirmLabel = stringResource(R.string.resume),
        dismissLabel = stringResource(R.string.quit_to_menu),
        confirmColor = Cyan,
        onConfirm = onResume,
        onDismiss = onQuit,
        onDismissRequest = onResume
    )
}

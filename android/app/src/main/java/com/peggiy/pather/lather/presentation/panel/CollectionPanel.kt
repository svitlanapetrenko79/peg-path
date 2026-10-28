package com.peggiy.pather.lather.presentation.panel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.core.theme.Cyan
import com.peggiy.pather.lather.core.theme.GlassBorder
import com.peggiy.pather.lather.core.theme.GlassBorderCyan
import com.peggiy.pather.lather.core.theme.GlassFill
import com.peggiy.pather.lather.core.theme.TextMuted
import com.peggiy.pather.lather.core.theme.TextPrimary
import com.peggiy.pather.lather.core.ui.SecondaryButton
import com.peggiy.pather.lather.core.ui.tokenDrawable
import com.peggiy.pather.lather.presentation.menu.TokenCardUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionPanel(
    tokens: List<TokenCardUi>,
    onSelect: (Int) -> Unit,
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
                text = stringResource(R.string.panel_collection),
                style = MaterialTheme.typography.labelLarge,
                color = Cyan
            )
            Spacer(modifier = Modifier.height(12.dp))
            tokens.chunked(3).forEach { chunk ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                ) {
                    chunk.forEach { token ->
                        TokenTile(
                            token = token,
                            onSelect = onSelect,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    for (index in chunk.size until 3) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(
                text = stringResource(R.string.close),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TokenTile(
    token: TokenCardUi,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onSelect(token.id) },
        enabled = token.owned,
        shape = RoundedCornerShape(18.dp),
        color = GlassFill,
        border = BorderStroke(1.dp, if (token.equipped) GlassBorderCyan else GlassBorder),
        tonalElevation = 0.dp,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(tokenDrawable(token.id)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alpha = if (token.owned) 1f else 0.35f,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(token.nameRes),
                style = MaterialTheme.typography.labelSmall,
                color = if (token.owned) TextPrimary else TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when {
                    token.equipped -> stringResource(R.string.equipped)
                    token.owned -> stringResource(R.string.unlocked)
                    else -> stringResource(R.string.locked_level_fmt, token.unlockLevel)
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (token.equipped) Cyan else TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

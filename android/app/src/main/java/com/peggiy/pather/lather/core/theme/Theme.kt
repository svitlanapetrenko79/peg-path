package com.peggiy.pather.lather.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PegPathColorScheme = darkColorScheme(
    primary = Violet,
    onPrimary = BgBase,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = TextPrimary,
    secondary = Cyan,
    onSecondary = BgBase,
    secondaryContainer = SurfaceCool,
    onSecondaryContainer = TextPrimary,
    tertiary = HotPink,
    onTertiary = BgBase,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfaceDeep,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = TrackInactive,
    error = HotPink,
    onError = BgBase,
    scrim = BgBase
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PegPathColorScheme,
        typography = AppTypography,
        content = content
    )
}

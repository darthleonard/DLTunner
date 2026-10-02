package com.darthleonard.dltunner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkTunerColorScheme = darkColorScheme(
    primary = NeonGreenPrimary,
    onPrimary = DarkBackground,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = AccentOrange,
    tertiary = AccentRed,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = OutlineDark
)

@Composable
fun DLTunnerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkTunerColorScheme,
        typography = Typography,
        content = content
    )
}

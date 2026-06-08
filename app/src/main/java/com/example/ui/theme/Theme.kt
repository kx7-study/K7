package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KX7DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPurple,
    tertiary = NeonGreen,
    background = CyberBlack,
    surface = DeepSpaceSlate,
    onPrimary = Color.Black,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    surfaceVariant = SurfaceCardDark,
    onSurfaceVariant = TextMutedGrey,
    error = WarningCrimson,
    outline = BorderCyberDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark mode for the 1% student identity
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KX7DarkColorScheme,
        typography = Typography,
        content = content
    )
}

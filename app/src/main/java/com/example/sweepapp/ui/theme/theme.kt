package com.example.sweepapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//Placeholders for now, UI design later
private val PlaceholderColors = lightColorScheme(
    primary = SweepPrimary,
    background = SweepBackground,
    surface = SweepBackground,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun SweepAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PlaceholderColors,
        typography = Typography(),
        content = content
    )
}
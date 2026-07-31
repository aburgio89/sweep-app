package com.example.sweepapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

//Placeholders for now, UI design later
private val PlaceholderColors = lightColorScheme(
    primary = SweepPrimary,
    background = SweepBackground
)

@Composable
fun SweepAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PlaceholderColors,
        typography = AppTypography,
        content = content
    )
}
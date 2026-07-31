package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onStartSweeps: () -> Unit,
    onOpenSettings: () -> Unit
) {
    ScreenScaffold(title = "Home") {
        Button(
            onClick = onStartSweeps,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Sweeps")
        }
        OutlinedButton(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Settings") }
    }
}
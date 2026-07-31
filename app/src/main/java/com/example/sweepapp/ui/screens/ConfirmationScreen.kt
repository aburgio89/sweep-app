package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ConfirmationScreen(
    wasFullSweep: Boolean,
    onReturnHome: () -> Unit
) {
    val title = if (wasFullSweep) "Confirm Full Sweep" else "Confirm Progress"
    val message = if (wasFullSweep) {
        "Congrats - full sweep complete!"
    } else {
        "Nice job on your progress so far!"
    }

    ScreenScaffold(title = title) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onReturnHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Return to Home")
        }
    }
}
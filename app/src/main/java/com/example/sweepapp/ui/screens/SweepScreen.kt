package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SweepScreen(
    sweepNumber: Int,
    sweepName: String,
    totalSweeps: Int,
    onComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val isLastSweep = sweepNumber == totalSweeps

    ScreenScaffold(title = "Sweep $sweepNumber: $sweepName") {
        Text(
            text = "Sweep $sweepNumber of $totalSweeps",
            textAlign = TextAlign.Center
        )
        LinearProgressIndicator(
            progress = { sweepNumber / totalSweeps.toFloat()},
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            //PLACEHOLDER
            text = "Placeholder for $sweepName sweep checklist/content."
        )
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLastSweep) "Complete Final Sweep" else "Complete & Continue")
        }
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stop")
        }
    }
}
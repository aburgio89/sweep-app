package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun DoomBoxCaptureScreen(
    onDone: () -> Unit
) {
    ScreenScaffold(title = "DOOM Box Capture") {
        Text(
            //PLACEHOLDER
            text = "Placeholder text for DoomBox page.",
            textAlign = TextAlign.Center
        )
        //Placeholder for DoomBox capture UI
        Text(
            "DoomBox capture UI goes here."
        )
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}
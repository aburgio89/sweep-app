package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.sweepapp.data.AppDataRepository

@Composable
fun DoomBoxCaptureScreen(
    onDone: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val today = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    }

    ScreenScaffold(title = "DOOM Box Capture") {
        Text(
            //PLACEHOLDER
            text = "Placeholder text for DoomBox page.",
            textAlign = TextAlign.Center
        )

        Text(
            text = "Date: $today")

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                if (it.isNotBlank()) showError = false
            },
            label = { Text("i.e., Doomy McDoomface") },
            isError = showError,
            supportingText = { if (showError) Text("You must give it a name!")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (name.isBlank()) {
                    showError = true
                } else {
                    AppDataRepository.addDoomBoxEntry(name = name.trim(), note = note)
                    onDone()
                    }
                },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        OutlinedButton(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Skip")
        }
    }
}
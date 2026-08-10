package com.example.sweepapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweepapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.navigation.Screen
import com.example.sweepapp.ui.theme.SweepAccent
import com.example.sweepapp.ui.theme.SweepAppTheme
import com.example.sweepapp.ui.theme.SweepBackground
import com.example.sweepapp.ui.theme.SweepPrimary

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
        Image(
            painter = painterResource(id = R.drawable.doombox),
            contentDescription = null,
            colorFilter = ColorFilter.tint(SweepAccent),
            alignment = Alignment.Center,
            modifier = Modifier.size(190.dp)
        )

        Text(
            text = "Date: $today")

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)){
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) showError = false
                },

                label = { Text("Name") },
                placeholder = { Text("Doomy McDoomface")},
                isError = showError,
                supportingText = { if (showError) Text("You must give it a name!")},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = SweepPrimary,
                    focusedPlaceholderColor = SweepPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (Optional)") },
                placeholder = { Text("Red polka dot bag")},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = SweepPrimary,
                    focusedPlaceholderColor = SweepPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.width(2.dp))

        Text(
            text="DOOM: Didn't Organize, Only Moved.",
            textAlign = TextAlign.Center,
            fontWeight = Bold
        )
        Text(
            text = "Leftover clutter without a home may be contained in a DOOM box. It is important to register your DOOM box so it is not forgotten.",
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.width(2.dp))

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
            Text("Save", color = SweepBackground)
        }

        OutlinedButton(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Skip", color = SweepPrimary)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DoomBoxPreview() {
    SweepAppTheme {
        DoomBoxCaptureScreen(
            onDone = {})
    }
}
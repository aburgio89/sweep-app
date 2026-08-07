package com.example.sweepapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.ui.theme.SweepAccent
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    onStartSweeps: () -> Unit,
    onOpenSettings: () -> Unit,
    onViewDoomBox: () -> Unit
) {
    val doomBoxEntries by AppDataRepository.doomBoxEntries.collectAsState()
    val lastFullSweepDate by AppDataRepository.lastFullSweepDate.collectAsState()
    val outstandingCount = doomBoxEntries.count {!it.resolved }
    val hasOutstanding = outstandingCount > 0
    val lastSweepText = lastFullSweepDate?.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        ?: "First time? Get Sweeping!"

    ScreenScaffold(title = "Home") {
        Text(
            text = "Outstanding DOOM Boxes: $outstandingCount",
            color = if (hasOutstanding) SweepAccent else MaterialTheme.colorScheme.onBackground,
            fontWeight = if (hasOutstanding) FontWeight.Bold else FontWeight.Normal,
            modifier = if (hasOutstanding) {
                Modifier.clickable(onClick = onViewDoomBox)
            } else Modifier)

        Text("Last full sweep completed: \n$lastSweepText")

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
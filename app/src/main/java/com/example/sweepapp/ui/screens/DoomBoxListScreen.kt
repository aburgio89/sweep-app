package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.data.DoomBoxEntry
import com.example.sweepapp.navigation.Screen
import com.example.sweepapp.ui.theme.SweepAccentColors
import com.example.sweepapp.ui.theme.SweepAppTheme
import com.example.sweepapp.ui.theme.SweepBackground
import com.example.sweepapp.ui.theme.SweepDarksOnly
import java.time.format.DateTimeFormatter

@Composable
fun DoomBoxListScreen(
    onBack: () -> Unit,
    onViewReport: () -> Unit
) {
    val allEntries by AppDataRepository.doomBoxEntries.collectAsState()
    val outstandingEntries = allEntries.filter { !it.resolved }

    var entryPendingResolve by remember { mutableStateOf<DoomBoxEntry?>(null) }

    ScreenScaffold(title = "Outstanding DOOM Boxes", onBack = onBack) {
        if (outstandingEntries.isEmpty()) {
            Text("Nothing outstanding -- great job!")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(outstandingEntries, key = { _, entry -> entry.id }) { index, entry ->
                    DoomBoxEntryCard(
                        entry = entry,
                        accentColor = SweepDarksOnly[index % SweepDarksOnly.size],
                        onResolveClick = { entryPendingResolve = entry }
                    )
                }
            }
        }
        TextButton(onClick = onViewReport) {
            Text("View Full History")
        }
    }

    entryPendingResolve?.let { entry ->
        AlertDialog(
            onDismissRequest = { entryPendingResolve = null },
            title = {
                Text(
                    text = "Resolve this item?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = SweepBackground
                )
                    },
            text = {
                Text(
                    text = "\"${entry.name}\" will be removed. This cannot be undone.",
                    color = SweepBackground)
                   },

            confirmButton = {
                TextButton(onClick = {
                    AppDataRepository.resolveDoomBoxEntry(entry.id)
                    entryPendingResolve = null
                }) {
                    Text("Resolve", color = SweepBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton =  {
                TextButton(onClick = { entryPendingResolve = null }) {
                    Text("Cancel", color = SweepBackground)
                }
            }
        )
    }
}

@Composable
fun DoomBoxEntryCard(
    entry: DoomBoxEntry,
    accentColor: Color,
    onResolveClick: () -> Unit) {

    val textColor = if (accentColor.luminance() > 0.5f) Color(0xFF32323B) else Color.White

    Card(
        colors = CardDefaults.cardColors(
            containerColor = accentColor,
            contentColor = textColor),
        modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.dateCreated.format(DateTimeFormatter.ofPattern("MMM d, yyyy")))
            Text(
                text = entry.name,
                style = MaterialTheme.typography.titleLarge
            )
            entry.note?.let { note ->
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyLarge) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onResolveClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = textColor,
                        contentColor = accentColor
                    )) {
                    Text("Resolve")
                }
            }
        }
    }
}

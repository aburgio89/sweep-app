package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.data.DoomBoxEntry
import java.time.format.DateTimeFormatter

@Composable
fun DoomBoxListScreen(
    onBack: () -> Unit
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
                items(outstandingEntries, key = { it.id }) { entry ->
                    DoomBoxEntryCard(
                        entry = entry,
                        onResolveClick = { entryPendingResolve = entry }
                    )
                }
            }
        }
    }
    entryPendingResolve?.let { entry ->
        AlertDialog(
            onDismissRequest = { entryPendingResolve = null },
            title = { Text("Resolve this item?") },
            text = { Text("\"${entry.name}\" will be removed. This cannot be undone.")},
            confirmButton = {
                TextButton(onClick = {
                    AppDataRepository.resolveDoomBoxEntry(entry.id)
                    entryPendingResolve = null
                }) {
                    Text("Resolve")
                }
            },
            dismissButton =  {
                TextButton(onClick = { entryPendingResolve = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DoomBoxEntryCard(entry: DoomBoxEntry, onResolveClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = entry.dateCreated.format(DateTimeFormatter.ofPattern("MMM d, yyyy")))
            Text(text = entry.name)
            entry.note?.let { note -> Text(text = note) }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onResolveClick) {
                    Text("Resolve")
                }
            }
        }
    }
}
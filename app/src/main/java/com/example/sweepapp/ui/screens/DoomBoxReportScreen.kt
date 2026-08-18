package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.data.DoomBoxEntry
import com.example.sweepapp.ui.theme.SweepWarning
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DoomBoxReportScreen(
    onBack: () -> Unit
) {
    val allEntries by AppDataRepository.doomBoxEntries.collectAsState()

    DoomBoxReportContent(
        allEntries = allEntries,
        onBack = onBack,
        onDelete = { entry -> AppDataRepository.removeDoomBoxEntry(entry.id) }
    )
}

@Composable
private fun DoomBoxReportContent(
    allEntries: List<DoomBoxEntry>,
    onBack: () -> Unit,
    onDelete: (DoomBoxEntry) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var entryPendingDelete by remember { mutableStateOf<DoomBoxEntry?>(null)}

    val generatedAt = remember {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a"))
    }
    val filteredEntries = remember(allEntries, searchQuery) {
        allEntries.filter { entry ->
            searchQuery.isBlank() ||
                    entry.name.contains(searchQuery, ignoreCase = true) ||
                    entry.note?.contains(searchQuery, ignoreCase = true) == true
        } .sortedByDescending { it.dateCreated }
    }

    ScreenScaffold(
        title = "DOOM Box Report", onBack = onBack
    ) {
        Text(
            text = "Generated: $generatedAt",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.6f))
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by name or note") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Results: ${filteredEntries.size}")

        if (filteredEntries.isEmpty()) {
            Text("No matches.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredEntries, key = { it.id }) { entry ->
                    ReportRow(entry = entry, onDeleteClick = { entryPendingDelete = entry})
                }
            }
        }
    }
    entryPendingDelete?.let { entry ->
        AlertDialog(
            onDismissRequest = { entryPendingDelete = null },
            title = { Text("Delete this item") },
            text = { Text("\"${entry.name} will be permanently deleted. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(entry)
                    entryPendingDelete = null
                }) {
                    Text("Delete", color = SweepWarning)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    entryPendingDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ReportRow(entry: DoomBoxEntry, onDeleteClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3F3F4A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.dateCreated.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.6f))
                )
                Text(
                    text = entry.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                entry.note?.let {
                    Text(text = it, color = Color.White.copy(alpha = 0.8f))
                }
                Text(
                    text = if (entry.resolved) "Resolved" else "Outstanding",
                    color = if (entry.resolved) Color(0xFF5EED8A) else SweepWarning
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Filled.Delete, contentDescription = "Delete entry",
                    tint = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}





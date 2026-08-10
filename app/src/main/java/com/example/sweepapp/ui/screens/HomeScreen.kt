package com.example.sweepapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweepapp.R
import com.example.sweepapp.data.AccountSettingsRepository
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.ui.theme.SweepAccent
import com.example.sweepapp.ui.theme.SweepAppTheme
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    onStartSweeps: () -> Unit,
    onOpenSettings: () -> Unit,
    onViewDoomBox: () -> Unit
) {
    val doomBoxEntries by AppDataRepository.doomBoxEntries.collectAsState()
    val lastFullSweepDate by AppDataRepository.lastFullSweepDate.collectAsState()
    val accountSettings by AccountSettingsRepository.settings.collectAsState()
    val outstandingCount = doomBoxEntries.count {!it.resolved }
    val hasOutstanding = outstandingCount > 0
    val lastSweepText = lastFullSweepDate?.format(DateTimeFormatter.ofPattern("MMM d"))
        ?: "N/A"

    val greeting = if (accountSettings.displayName.isNotBlank()) {
        "Welcome back, ${accountSettings.displayName}."
    } else {
        "Shall we get started?"
    }

    ScreenScaffold(
        title = "Home",
        titleContent = {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
            )
        }) {
        Text(
            text = greeting,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Max)
        ){
            StatCard(
                label = "Outstanding Doom Boxes",
                value = if (hasOutstanding) "$outstandingCount" else "✓",
                accent = if (hasOutstanding) SweepAccent else Color(0xFF5EED8A),
                onClick = if (hasOutstanding) onViewDoomBox else null,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            StatCard(
                label = "Last Full Sweep",
                value = lastSweepText,
                accent = Color.White.copy(alpha = 0.4f),
                onClick = null,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }

        Button(
            onClick = onStartSweeps,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SweepAccent,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Text("Start Sweeping",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
        }

        TextButton(
            onClick = onOpenSettings
        ) { Text("Settings", color = Color.White.copy(alpha = 0.7f), fontSize = 20.sp) }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    accent: Color,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3F3F4A)),
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .let { if (onClick != null) it else it}
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = accent,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )
        }
    }
    
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    SweepAppTheme {
        HomeScreen(
            onStartSweeps = {},
            onViewDoomBox = {},
            onOpenSettings = {}
        )
    }
}
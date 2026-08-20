package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sweepapp.data.AccountSettings
import com.example.sweepapp.data.AccountSettingsRepository
import com.example.sweepapp.data.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun AccountSettingsScreen(
    onBack: () -> Unit,
    onSignedOut: () -> Unit
) {
    val settings by AccountSettingsRepository.settings.collectAsState()

    AccountSettingsContent(
        settings = settings,
        onBack = onBack,
        onSaveName = { newName -> AccountSettingsRepository.updateDisplayName(newName) },
        onDoomBoxAlertsEnabledChange = { AccountSettingsRepository.updateDoomBoxAlertsEnabled(it) },
        onDoomBoxIntervalChange = { AccountSettingsRepository.updateDoomBoxAlertIntervalWeeks(it) },
        onInactivityAlertsEnabledChange = { AccountSettingsRepository.updateInactivityAlertsEnabled(it) },
        onSignOut = {
            AuthRepository.signOut()
            onSignedOut()
        }
    )
}

@Composable
private fun AccountSettingsContent(
    settings: AccountSettings,
    onBack: () -> Unit,
    onSaveName: (String) -> Unit,
    onDoomBoxAlertsEnabledChange: (Boolean) -> Unit,
    onDoomBoxIntervalChange: (Int) -> Unit,
    onInactivityAlertsEnabledChange: (Boolean) -> Unit,
    onSignOut: () -> Unit
) {
    var nameDraft by remember(settings.displayName) { mutableStateOf(settings.displayName) }
    var nameChanged = nameDraft.trim() != settings.displayName && nameDraft.isNotBlank()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    ScreenScaffold(title = "Account Settings", onBack = onBack, snackbarHostState = snackbarHostState) {

        Text("Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = nameDraft,
            onValueChange = { nameDraft = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        if (nameChanged) {
            Button(
                onClick = {
                    focusManager.clearFocus() //Should suppress on-screen keyboard
                    val trimmedName = nameDraft.trim()
                    onSaveName(trimmedName)
                    scope.launch { snackbarHostState.showSnackbar("Name Updated") }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Name")
            }
        }

        OutlinedTextField(
            value = settings.email,
            onValueChange = {},
            enabled = false,
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Divider()

        Text("Notifications", style=MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text= "Alert me about outstanding DOOM Boxes", modifier = Modifier.weight(1f).padding(2.dp))
            Switch(
                checked = settings.doomBoxAlertsEnabled,
                onCheckedChange = onDoomBoxAlertsEnabledChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF96D4A6))
            )
        }

        if (settings.doomBoxAlertsEnabled) {
            Text("Remind me after: ")
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                (1..4).forEach { weeks ->
                    SegmentedButton(
                        selected = settings.doomBoxAlertIntervalWeeks == weeks,
                        onClick = { onDoomBoxIntervalChange(weeks) },
                        shape = SegmentedButtonDefaults.itemShape(index = weeks - 1, count = 4)
                    ){
                        Text("${weeks} w.")
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text ="Alert me if I haven't used the app in a while",
                modifier = Modifier.weight(1f).padding(2.dp))
            Switch(
                checked = settings.inactivityAlertsEnabled,
                onCheckedChange = onInactivityAlertsEnabledChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF96D4A6))
            )
        }

        Divider()
        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out")
        }

        Text("Sweep App v0.1")
    }
}
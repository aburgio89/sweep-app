package com.example.sweepapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AccountSettingsScreen(
    onBack: () -> Unit
) {
    ScreenScaffold(title = "Account Settings", onBack = onBack) {
        //Placeholder for now. Will add personal info, notifications, preferences, etc.
        Text("Account settings will go here.")
    }
}
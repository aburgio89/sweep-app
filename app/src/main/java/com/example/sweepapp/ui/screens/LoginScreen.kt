package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(
    onLogIn: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}

    ScreenScaffold(title = "Log In") {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it},
            label = { org.w3c.dom.Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { org.w3c.dom.Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onLogIn,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }
    }
}
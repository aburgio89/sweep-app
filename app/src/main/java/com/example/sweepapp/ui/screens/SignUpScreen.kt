package com.example.sweepapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

//Email format validation
private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

//Password format validation
private fun isValidPassword(password: String): Boolean {
    return password.length >= 6 &&
            password.any { it.isLetter() } &&
            password.any { it.isDigit() }
}

@Composable
fun SignUpScreen(
    onSignUp: suspend (email: String, password: String) -> Result<Unit>,
    onSignUpSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    ScreenScaffold(title = "Sign Up", onBack = onBackToLogin) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            textStyle = TextStyle(color = Color.White),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            textStyle = TextStyle(color = Color.White),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Password") },
            textStyle = TextStyle(color = Color.White),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Password must be at least 6 characters, including 1 letter and 1 number.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.6f))
        )

        errorMessage?.let {
            Text(text = it, color = Color(0xFFFF0000))
        }

        Button(
            onClick = {
                val trimmedEmail = email.trim()
                errorMessage = when {
                    !emailRegex.matches(trimmedEmail) ->
                        "Please enter a valid email address."
                    !isValidPassword(password) ->
                        "Password does not meet requirements."
                    password != confirmPassword ->
                        "Passwords do not match."
                    else -> null
                }

                if (errorMessage == null) {
                    isLoading = true
                    scope.launch {
                        val result = onSignUp(trimmedEmail, password)
                        isLoading = false
                        result.onSuccess { onSignUpSuccess() }.onFailure { errorMessage = it.message ?: "Sign up failed." }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Create Account")
            }
        }
    }
}
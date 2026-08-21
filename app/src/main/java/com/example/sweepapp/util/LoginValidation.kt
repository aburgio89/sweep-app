package com.example.sweepapp.util

fun validateLoginInput(email: String, password: String): String? {
    return when {
        email.isBlank() -> "Enter your email."
        password.isBlank() -> "Enter your password."
        else -> null
    }
}
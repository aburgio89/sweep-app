package com.example.sweepapp.data

data class AccountSettings(
    val displayName: String = "",
    val email: String = "",
    val doomBoxAlertsEnabled: Boolean = true,
    val doomBoxAlertIntervalWeeks: Int = 1,
    val inactivityAlertsEnabled: Boolean = true
)
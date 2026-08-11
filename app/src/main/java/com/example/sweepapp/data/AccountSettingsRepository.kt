package com.example.sweepapp.data

import android.R.attr.enabled
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AccountSettingsRepository {
    private val _settings = MutableStateFlow(
        AccountSettings(
            displayName = "",
            email = "example@example.com" //Placeholder until Firebase setup
        )
    )
    val settings: StateFlow<AccountSettings> = _settings.asStateFlow()

    fun updateDisplayName(name: String) {
        _settings.value = _settings.value.copy(displayName = name)
    }

    fun updateDoomBoxAlertsEnabled(enabled: Boolean) {
        _settings.value = _settings.value.copy(doomBoxAlertsEnabled = enabled)
    }

    fun updateDoomBoxAlertIntervalWeeks(weeks: Int) {
        _settings.value = _settings.value.copy(doomBoxAlertIntervalWeeks = weeks)
    }

    fun updateInactivityAlertsEnabled(enabled: Boolean) {
        _settings.value = _settings.value.copy(inactivityAlertsEnabled = enabled)
    }

    fun updateEmailFromAuth(email: String) {
        _settings.value = _settings.value.copy(email = email)
    }
}
package com.example.sweepapp.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AccountSettingsRepository : UserScopedFirestoreRepository() {
    private val _settings = MutableStateFlow(AccountSettings())
    val settings: StateFlow<AccountSettings> = _settings.asStateFlow()

    override fun onStart(userDoc: DocumentReference) {
        _settings.value = _settings.value.copy(email = AuthRepository.currentUserEmail ?: "")

    track(
        userDoc.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                _settings.value = _settings.value.copy(
                    displayName = snapshot.getString("displayName") ?: "",
                    doomBoxAlertsEnabled = snapshot.getBoolean("doomBoxAlertsEnabled") ?: true,
                    doomBoxAlertIntervalWeeks = (snapshot.getLong("doomBoxAlertIntervalWeeks") ?: 1L).toInt(),
                    inactivityAlertsEnabled = snapshot.getBoolean("inactivityAlertsEnabled") ?: true
                )
            } else {
                userDoc.set(
                    mapOf(
                        "displayName" to "",
                        "doomBoxAlertsEnabled" to true,
                        "doomBoxAlertIntervalWeeks" to 1,
                        "inactivityAlertsEnabled" to true
                    ),
                    SetOptions.merge()
                )
            }
        }
    )
}

    override fun onStop() {
        _settings.value = AccountSettings()
    }

    fun updateDisplayName(name: String) {
        currentUserDoc()?.update("displayName", name)
    }

    fun updateDoomBoxAlertsEnabled(enabled: Boolean) {
        currentUserDoc()?.update("doomBoxAlertsEnabled", enabled)
    }

    fun updateDoomBoxAlertIntervalWeeks(weeks: Int) {
        currentUserDoc()?.update("doomBoxAlertIntervalWeeks", weeks)
    }

    fun updateInactivityAlertsEnabled(enabled: Boolean) {
        currentUserDoc()?.update("inactivityAlertsEnabled", enabled)
    }
}
package com.example.sweepapp.data

import android.R.attr.enabled
import android.accounts.Account
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AccountSettingsRepository {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var settingsListener: ListenerRegistration? = null

    private val _settings = MutableStateFlow(AccountSettings())
    val settings: StateFlow<AccountSettings> = _settings.asStateFlow()

    fun start(uid: String) {
        stop()
        _settings.value = _settings.value.copy(email = AuthRepository.currentUserEmail ?: "")

        val userDoc = db.collection("users").document(uid)

        settingsListener = userDoc.addSnapshotListener { snapshot, _ ->
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

    }

    fun stop() {
        settingsListener?.remove()
        settingsListener = null
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

    private fun currentUserDoc() =
        AuthRepository.currentUserId?.let { uid -> db.collection("users").document(uid)}
}
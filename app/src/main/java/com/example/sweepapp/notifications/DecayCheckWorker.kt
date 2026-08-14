package com.example.sweepapp.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DecayCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Result.success()
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("users").document(uid)

        val userDoc = userDocRef.get().await()
        if (!userDoc.exists()) return Result.success()

        val doomBoxAlertsEnabled = userDoc.getBoolean("doomBoxAlertsEnabled") ?: true
        val intervalWeeks = (userDoc.getLong("doomBoxAlertIntervalWeeks") ?: 1L).toInt()
        val inactivityAlertsEnabled = userDoc.getBoolean("inactivityAlertsEnabled") ?: true

        if (doomBoxAlertsEnabled) {
            val entries = userDocRef.collection("doomBoxEntries").get().await()
            val today = LocalDate.now().toEpochDay()
            val intervalDays = intervalWeeks * 7L

            val staleCount = entries.documents.count { doc ->
                val resolved = doc.getBoolean("resolved") ?: false
                val created = doc.getTimestamp("dateCreated")?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                !resolved && created != null && (today - created.toEpochDay()) >= intervalDays
            }

            if (staleCount > 0) {
                NotificationHelper.showDoomBoxReminder(applicationContext)
            }
        }
        if (inactivityAlertsEnabled) {
            val lastSweepDate = userDoc.getTimestamp("lastFullSweepDate")?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
            val daysSince = lastSweepDate?.let{ ChronoUnit.DAYS.between(it, LocalDate.now()) } ?: Long.MAX_VALUE
            if (daysSince >= 30) {
                NotificationHelper.showDecayReminder(applicationContext)
            }
        }
        return Result.success()
    }
}
package com.example.sweepapp.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    private const val WORK_NAME = "decay_check_worker"

    fun schedulerPeriodicCheck(context: Context) {
        val request = PeriodicWorkRequestBuilder<DecayCheckWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, request)
    }
}
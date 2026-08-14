package com.example.sweepapp

import android.app.Application
import com.example.sweepapp.notifications.NotificationHelper
import com.example.sweepapp.notifications.NotificationScheduler

class SweepApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannels(this)
        NotificationScheduler.schedulerPeriodicCheck(this)
    }
}
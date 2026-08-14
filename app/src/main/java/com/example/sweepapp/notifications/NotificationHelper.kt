package com.example.sweepapp.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
    const val DOOM_BOX_CHANNEL_ID = "doom_box_reminders"
    const val DECAY_CHANNEL_ID = "decay_reminders"

    private const val DOOM_BOX_NOTIFICATION_ID = 1001
    private const val DECAY_NOTIFICATION_ID = 1002

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(
            NotificationChannel(DOOM_BOX_CHANNEL_ID, "DOOM Box reminders", NotificationManager.IMPORTANCE_DEFAULT)
                .apply { description = "Reminders about unresolved DOOM Boxes"}
        )

        manager.createNotificationChannel(
            NotificationChannel(DECAY_CHANNEL_ID, "Full sweep reminders", NotificationManager.IMPORTANCE_DEFAULT)
                .apply { description = "Reminders when it's been a while since your last full sweep"}
        )
    }

    private fun hasPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun showDoomBoxReminder(context: Context) {
        if (!hasPermission(context)) return
        val notification = NotificationCompat.Builder(context, DOOM_BOX_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) //Swap out for favicon once made
            .setContentTitle("Your DOOM box needs attention.")
            .setContentText("You have unresolved DOOM boxes. Don't let them stagnate!")
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(DOOM_BOX_NOTIFICATION_ID, notification)
    }

    @SuppressLint("MissingPermission")
    fun showDecayReminder(context: Context) {
        if (!hasPermission(context)) return
        val notification = NotificationCompat.Builder(context, DECAY_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) //Swap out for favicon once made
            .setContentTitle("It's been a while.")
            .setContentText("You haven't completed a sweep in over a month. Let's get the ball rolling!")
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(DECAY_NOTIFICATION_ID, notification)
    }
}
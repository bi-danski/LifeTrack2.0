package org.lifetrack.ltapp.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object DroidNotification {
    const val CHANNEL_ID = "MED_REMINDER_CH"

    fun createNotificationChannel(context: Context) {
        val name = "Medication Reminder"
        val descriptionText = "Notification for scheduled doses"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
            setShowBadge(true)
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
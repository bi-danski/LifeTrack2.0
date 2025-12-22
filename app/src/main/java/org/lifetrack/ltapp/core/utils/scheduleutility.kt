package org.lifetrack.ltapp.core.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar
import org.lifetrack.ltapp.core.broadcast.ReminderReceiver
import org.lifetrack.ltapp.model.data.dclass.Prescription


object ScheduleUtility {

    private const val TAG = "ScheduleUtility"

    fun scheduleReminder(context: Context, prescription: Prescription) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("MED_NAME", prescription.medicationName)
            action = "ACTION_REMIND_${prescription.id}"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prescription.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
             set(Calendar.HOUR_OF_DAY, 20)
             set(Calendar.MINUTE, 0)
             if (before(Calendar.getInstance())) { add(Calendar.DATE, 1) }
        }

        val triggerTime = System.currentTimeMillis() + 10000

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            Log.d(TAG, "Reminder scheduled for ${prescription.medicationName}")
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException: Cannot schedule exact alarm", e)
        }
    }

    fun cancelReminder(context: Context, prescription: Prescription) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = "ACTION_REMIND_${prescription.id}"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prescription.id.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Reminder cancelled for ${prescription.medicationName}")
        }
    }
}
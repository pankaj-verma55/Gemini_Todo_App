package com.example.todoapp.Data.Worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.todoapp.Data.Receiver.TodoAlarmReceiver

class NotificationScheduler(
    private val context: Context
) {
    private val alarmManager =
        context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

    fun scheduleTask(
        taskId: Long,
        title: String,
        description: String,
        taskTimeMillis: Long
    ) {

        if (taskTimeMillis <= System.currentTimeMillis()) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e(
                    "NotificationScheduler",
                    "Exact alarm permission is not granted"
                )
                return
            }
        }

        val intent = Intent(
            context,
            TodoAlarmReceiver::class.java
        ).apply {

            putExtra("taskId", taskId)
            putExtra("title", title)
            putExtra("description", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if (alarmManager.canScheduleExactAlarms()) {

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    taskTimeMillis,
                    pendingIntent
                )
            }

        } else {

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                taskTimeMillis,
                pendingIntent
            )
        }
    }

    fun cancelTask(taskId: Int) {
        val intent = Intent(
            context,
            TodoAlarmReceiver::class.java
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

        pendingIntent.cancel()
    }
}
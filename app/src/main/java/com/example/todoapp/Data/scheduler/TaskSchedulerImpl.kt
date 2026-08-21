package com.example.todoapp.Data.scheduler

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todoapp.Data.Alarm.TaskReminderWorker
import com.example.todoapp.Domain.TaskScheduler
import java.util.concurrent.TimeUnit

class TaskSchedulerImpl(
    private val context: Context
) : TaskScheduler {
//    private val alarmManager =
//        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleReminder(
        taskId: Int,
        title: String,
        description: String,
        taskTimeMillis: Long
    ) {

        val currentTime = System.currentTimeMillis()

        val delay = taskTimeMillis - currentTime

        if (delay <= 0) {
            return
        }

        val request =
            OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInputData(
                    workDataOf(
                        "taskId" to taskId,
                        "title" to title
                    )
                )
                .setInitialDelay(
                    delay,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager
            .getInstance(context)
            .enqueue(request)
    }

    override fun scheduleAlarm(
        taskId: Int,
        title: String,
        description: String,
        taskTimeMillis: Long
    ) {
//        val intent = Intent(context, NotificationReceiver::class.java).apply {
//            putExtra("task_id", taskId)
//            putExtra("title", title)
//            putExtra("description", description)
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            taskId,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        alarmManager.setAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            taskTimeMillis,
//            pendingIntent
//        )

    }
}
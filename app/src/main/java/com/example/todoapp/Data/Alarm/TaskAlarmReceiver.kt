package com.example.todoapp.Data.Alarm

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TaskReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {

        return try {

            val title =
                inputData.getString("title") ?: "Todo"

            val description =
                inputData.getString("description") ?: ""

            showNotification(
                title = title,
                description = description
            )

            Result.success()

        } catch (e: Exception) {

            Log.e(
                "TaskReminderWorker",
                "Notification failed",
                e
            )

            Result.failure()
        }
    }

    private fun showNotification(
        title: String,
        description: String
    ) {
        // Notification code
    }

}
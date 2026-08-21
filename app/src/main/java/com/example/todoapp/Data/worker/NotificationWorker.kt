package com.example.todoapp.Data.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.R

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val title = inputData.getString("title") ?: "Todo _title"
        val description = inputData.getString("todo description") ?: ""
        Log.e("NotificationWorker", "doWork: $title   $description")

        showNotification(title, description)

        return Result.success()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(
        title: String,
        description: String
    ) {

        val channelId = "todo_channel"

        val notificationManager =
            applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

//        val channel = NotificationChannel(
//            channelId,
//            "Todo Notifications",
//            NotificationManager.IMPORTANCE_HIGH
//        ).apply {
//            this.description = "Todo task notifications"
//        }
//
//        notificationManager.createNotificationChannel(channel)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Todo Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description = "Todo task notifications"
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Android 13+
        if (
            Build.VERSION.SDK_INT >= 33 &&
            applicationContext.checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.ic_app)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(applicationContext)
            .notify(
                System.currentTimeMillis().toInt(),
                notification
            )
    }
}


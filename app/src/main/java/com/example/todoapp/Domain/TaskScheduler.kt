package com.example.todoapp.Domain

interface TaskScheduler {

    fun scheduleReminder(
        taskId: Int,
        title: String,
        description: String,
        taskTimeMillis: Long
    )

    fun scheduleAlarm(
        taskId: Int,
        title: String,
        description: String,
        taskTimeMillis: Long
    )
}
package com.example.todoapp.Domain.UseCase

import com.example.todoapp.Domain.TaskScheduler

class ScheduleTaskUseCase(
    private val taskScheduler: TaskScheduler
) {

    operator fun invoke(
        taskId: Int,
        title: String,
        description:String,
        taskTimeMillis: Long
    ) {
        taskScheduler.scheduleReminder(
            taskId,
            title,
            description,
            taskTimeMillis
        )

        taskScheduler.scheduleAlarm(
            taskId,
            title,
            description,
            taskTimeMillis
        )
    }
}
package com.example.todoapp.Ui.Adapter.Viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.Data.scheduler.TaskSchedulerImpl
import com.example.todoapp.Domain.UseCase.ScheduleTaskUseCase

class TodoViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            // Create dependencies required by TodoViewModel
            val taskScheduler = TaskSchedulerImpl(application)
            val scheduleTaskUseCase = ScheduleTaskUseCase(taskScheduler)

            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(application, scheduleTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
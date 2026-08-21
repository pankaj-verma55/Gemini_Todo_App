package com.example.todoapp.Ui.Adapter.Viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Data.TodoDataBase
import com.example.todoapp.Data.TodoDataRepository
import com.example.todoapp.Data.Worker.NotificationScheduler
import com.example.todoapp.Domain.UseCase.AddTodoUser
import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.Domain.UseCase.ScheduleTaskUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

open class TodoViewModel(application: Application,
                         private val scheduleTaskUseCase: ScheduleTaskUseCase
) : AndroidViewModel(application = application) {
    // In TodoViewModel.kt
    private val db = TodoDataBase.getDatabase(application)

    private val repository = TodoDataRepository(db.todoDao())
    private val addTodoUser = AddTodoUser(repository)

    private val notificationScheduler =
        NotificationScheduler(
            application
        )
    open val todoItems = repository.getTodoData().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )
    fun addTodo(fullDate:String, listSelectedItem: String, title: String, description: String, time: String, done: Boolean) {
        viewModelScope.launch {
            Log.d("TODO_TEST", "addTodo() called")
            val todoId = addTodoUser.execute(
                fullDate = fullDate,
                listSelectedItem = listSelectedItem,
                title = title,
                description = description,
                time = time,
                done = done
            )
            val taskTimeMillis = getTaskTimeMillis(
                fullDate,
                time
            )
            notificationScheduler.scheduleTask(
                taskId = todoId,
                title = title,
                description = description,
                taskTimeMillis = taskTimeMillis
            )
        }



    }

    fun editTodo(todo: TodoItem, newTitle: String, description: String, done: Boolean) {
        viewModelScope.launch {
            if (newTitle.isNotBlank())
                repository.updateTodoData(
                    todo.copy(title = newTitle, description = description, done = done)
                )
        }
    }

    fun deleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            notificationScheduler.cancelTask(todo.id)
            repository.deleteTodoData(todo)
        }
    }
    fun scheduleTask(
        taskId: Int,
        title: String,
        description: String,
        taskTimeMillis: Long
    ) {
        scheduleTaskUseCase(
            taskId,
            title,
            description,
            taskTimeMillis
        )
    }
    fun getTaskTimeMillis(
        date: String,
        time: String
    ): Long {

        val parts = date.split("/")

        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()

        val timeParts = time.split(":")

        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        return Calendar.getInstance().apply {

            set(
                year,
                month - 1,
                day,
                hour,
                minute,
                0
            )

            set(Calendar.MILLISECOND, 0)

        }.timeInMillis
    }
}
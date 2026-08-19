package com.example.todoapp.Ui.Adapter.Viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todoapp.Data.TodoDataBase
import com.example.todoapp.Data.TodoDataRepository
import com.example.todoapp.Domain.AddTodoUser
import com.example.todoapp.Domain.TodoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class TodoViewModel(application: Application) : AndroidViewModel(application = application) {
    // In TodoViewModel.kt
    private val db = TodoDataBase.getDatabase(application)

    private val repository = TodoDataRepository(db.todoDao())
    private val addTodoUser = AddTodoUser(repository)

    open val todoItems = repository.getTodoData().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )
    fun addTodo(fullDate:String,listSelectedItem: String,title: String, description: String, done: Boolean) {
        viewModelScope.launch {
            Log.d("TODO_TEST", "addTodo() called")
            addTodoUser.execute(fullDate,listSelectedItem,title, description, done)
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
            repository.deleteTodoData(todo)
        }
    }
}
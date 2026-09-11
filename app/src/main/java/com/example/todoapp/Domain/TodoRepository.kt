package com.example.todoapp.Domain

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodoData(): Flow<List<TodoItem>>
    suspend fun updateTodoData(todo: TodoItem)
    suspend fun createTodoData(todo: TodoItem): Long
    suspend fun deleteTodoData(todo: TodoItem)
}
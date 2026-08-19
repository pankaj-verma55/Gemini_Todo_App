package com.example.todoapp.Domain

import android.util.Log

class AddTodoUser(private val repository: TodoRepository) {
    suspend fun execute(
        fullDate: String,
        listSelectedItem: String,
        title: String,
        description: String,
        done: Boolean
    ) {
        repository.createTodoData(
            TodoItem(
                fullDate = fullDate,
                listSelectedItem = listSelectedItem,
                title = title,
                description = description,
                done = done
            )
        )
    }
}
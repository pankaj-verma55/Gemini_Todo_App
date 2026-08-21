package com.example.todoapp.Domain.UseCase

import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.Domain.TodoRepository

class AddTodoUser(private val repository: TodoRepository) {
    suspend fun execute(
        fullDate: String,
        listSelectedItem: String,
        title: String,
        description: String,
        time: String,
        done: Boolean
    ): Long {
        return repository.createTodoData(
            TodoItem(
                fullDate = fullDate,
                listSelectedItem = listSelectedItem,
                title = title,
                description = description,
                time = time,
                done = done
            )
        )
    }
}
package com.example.todoapp.Data

import com.example.todoapp.Data.DataClass.TodoDataItem
import com.example.todoapp.Domain.TodoItem
import com.example.todoapp.Domain.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoDataRepository(private val dao: TodoDao): TodoRepository {
    override fun getTodoData(): Flow<List<TodoItem>> {
        return dao.getTodoData().map { list ->
            list.map { entity ->
                TodoItem(
                    id = entity.id,
                    fullDate = entity.fullDate,
                    listSelectedItem = entity.listSelectedItem,
                    title = entity.title,
                    description = entity.description,
                    done = entity.done
                )
            }
        }
    }

    override suspend fun updateTodoData(todo: TodoItem) {
        return dao.updateTodoData(TodoDataItem(todo.id, todo.fullDate,todo.listSelectedItem,todo.title, todo.description, todo.done))
    }

    override suspend fun createTodoData(todo: TodoItem) {
        return dao.insertTodo(TodoDataItem(todo.id,todo.fullDate,todo.listSelectedItem, todo.title, todo.description, todo.done))
    }

    override suspend fun deleteTodoData(todo: TodoItem) {
        return dao.deleteTodoData(TodoDataItem(todo.id, todo.fullDate,todo.listSelectedItem,todo.title, todo.description , todo.done))
    }
}
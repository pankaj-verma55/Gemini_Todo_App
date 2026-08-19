package com.example.todoapp.Ui.Adapter.interfaces

import com.example.todoapp.Domain.TodoItem

interface OnCategoryClickListener {
    fun onCategoryClick(category: String)
    fun onDeleteTodo(todo: List<TodoItem>)
}
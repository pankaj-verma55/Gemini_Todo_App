package com.example.todoapp.Domain

data class TodoItem(
    val id: Int=0,
    val fullDate: String,
    val listSelectedItem: String,
    val title: String,
    val description: String,
    val done: Boolean
)

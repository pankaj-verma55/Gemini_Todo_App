package com.example.todoapp.Data.DataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_database")
data class TodoDataItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val fullDate: String,
    val listSelectedItem: String,
    val title: String,
    val description: String,
    val time: String,
    var done: Boolean = false
)
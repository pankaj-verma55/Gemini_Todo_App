package com.example.todoapp.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "todo_database")
data class DateItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val dayName: String,
    val localDate: LocalDate
)
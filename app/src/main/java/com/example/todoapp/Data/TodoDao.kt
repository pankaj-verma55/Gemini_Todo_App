package com.example.todoapp.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.Data.DataClass.TodoDataItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_database")
    fun getTodoData(): Flow<List<TodoDataItem>>

    @Update
    suspend fun updateTodoData(todo: TodoDataItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoDataItem)

    @Delete
    suspend fun deleteTodoData(todo: TodoDataItem)
}
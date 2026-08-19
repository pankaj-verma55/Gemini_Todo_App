package com.example.todoapp.Data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            ALTER TABLE todo_database
            ADD COLUMN fullDate TEXT NOT NULL DEFAULT ''
            """.trimIndent()
        )

        db.execSQL(
            """
            ALTER TABLE todo_database
            ADD COLUMN listSelectedItem TEXT NOT NULL DEFAULT ''
            """.trimIndent()
        )
    }
}
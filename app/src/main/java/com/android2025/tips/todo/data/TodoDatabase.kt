package com.android2025.tips.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoEntity::class], version = 1, exportSchema = true)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        const val NAME = "todo.db"
    }
}

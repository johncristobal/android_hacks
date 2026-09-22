package com.android2025.tips.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)

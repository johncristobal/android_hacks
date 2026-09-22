package com.android2025.tips.todo.domain

data class Todo(
    val id: Long,
    val title: String,
    val description: String,
    val isDone: Boolean,
    val createdAt: Long,
)

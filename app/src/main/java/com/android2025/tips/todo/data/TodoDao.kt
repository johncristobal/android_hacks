package com.android2025.tips.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY isDone ASC, createdAt DESC, id DESC")
    fun observeAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoEntity?

    // REPLACE so that restoring a deleted item (same id) is idempotent
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity): Long

    // Only touches title/description so a concurrent checkbox toggle is never overwritten
    @Query("UPDATE todos SET title = :title, description = :description WHERE id = :id")
    suspend fun updateContent(id: Long, title: String, description: String)

    @Query("UPDATE todos SET isDone = :isDone WHERE id = :id")
    suspend fun setDone(id: Long, isDone: Boolean)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun delete(id: Long)
}

package com.android2025.tips.todo.data

import com.android2025.tips.todo.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TodoRepository {
    fun observeTodos(): Flow<List<Todo>>
    suspend fun getTodo(id: Long): Todo?
    suspend fun create(title: String, description: String): Long
    suspend fun updateContent(id: Long, title: String, description: String)
    suspend fun setDone(id: Long, isDone: Boolean)
    suspend fun delete(id: Long)

    /** Re-inserts a previously deleted to-do with its original id and position. */
    suspend fun restore(todo: Todo)
}

class RoomTodoRepository(private val dao: TodoDao) : TodoRepository {

    override fun observeTodos(): Flow<List<Todo>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getTodo(id: Long): Todo? = dao.getById(id)?.toDomain()

    override suspend fun create(title: String, description: String): Long =
        dao.insert(TodoEntity(title = title, description = description))

    override suspend fun updateContent(id: Long, title: String, description: String) =
        dao.updateContent(id, title, description)

    override suspend fun setDone(id: Long, isDone: Boolean) = dao.setDone(id, isDone)

    override suspend fun delete(id: Long) = dao.delete(id)

    override suspend fun restore(todo: Todo) {
        dao.insert(todo.toEntity())
    }
}

private fun TodoEntity.toDomain() = Todo(id, title, description, isDone, createdAt)

private fun Todo.toEntity() = TodoEntity(id, title, description, isDone, createdAt)

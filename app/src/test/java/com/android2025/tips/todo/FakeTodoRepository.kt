package com.android2025.tips.todo

import com.android2025.tips.todo.data.TodoRepository
import com.android2025.tips.todo.domain.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTodoRepository(initial: List<Todo> = emptyList()) : TodoRepository {

    private val todos = MutableStateFlow(initial)
    private var nextId = (initial.maxOfOrNull { it.id } ?: 0L) + 1

    var writeCount = 0
        private set

    override fun observeTodos(): Flow<List<Todo>> = todos.map { list ->
        list.sortedWith(compareBy<Todo> { it.isDone }.thenByDescending { it.createdAt })
    }

    override suspend fun getTodo(id: Long): Todo? = todos.value.firstOrNull { it.id == id }

    override suspend fun create(title: String, description: String): Long {
        writeCount++
        val id = nextId++
        todos.update { it + Todo(id, title, description, isDone = false, createdAt = id) }
        return id
    }

    override suspend fun updateContent(id: Long, title: String, description: String) {
        writeCount++
        todos.update { list ->
            list.map { if (it.id == id) it.copy(title = title, description = description) else it }
        }
    }

    override suspend fun setDone(id: Long, isDone: Boolean) {
        writeCount++
        todos.update { list -> list.map { if (it.id == id) it.copy(isDone = isDone) else it } }
    }

    override suspend fun delete(id: Long) {
        writeCount++
        todos.update { list -> list.filterNot { it.id == id } }
    }

    override suspend fun restore(todo: Todo) {
        writeCount++
        todos.update { list -> list.filterNot { it.id == todo.id } + todo }
    }

    fun snapshot(): List<Todo> = todos.value
}

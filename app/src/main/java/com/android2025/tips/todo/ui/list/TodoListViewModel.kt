package com.android2025.tips.todo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android2025.tips.todo.data.TodoRepository
import com.android2025.tips.todo.domain.Todo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TodoListUiState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
)

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    val uiState: StateFlow<TodoListUiState> = repository.observeTodos()
        .map { TodoListUiState(todos = it, isLoading = false) }
        .catch { emit(TodoListUiState(isLoading = false, hasError = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TodoListUiState(),
        )

    fun onToggle(id: Long, isDone: Boolean) {
        viewModelScope.launch { repository.setDone(id, isDone) }
    }

    fun onDelete(todo: Todo) {
        viewModelScope.launch { repository.delete(todo.id) }
    }

    fun onUndoDelete(todo: Todo) {
        viewModelScope.launch { repository.restore(todo) }
    }
}

package com.android2025.tips.todo.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android2025.tips.todo.data.TodoRepository
import com.android2025.tips.todo.domain.Todo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TodoDetailState {
    data object Loading : TodoDetailState

    /** The requested to-do no longer exists (e.g. deleted or invalid id). */
    data object NotFound : TodoDetailState

    /** [original] is null when creating a new to-do. */
    data class Loaded(val original: Todo?) : TodoDetailState
}

/**
 * The text being edited lives in the screen (`rememberSaveable`, so it survives rotation and
 * process death); this ViewModel loads the original and decides what to persist on back.
 */
class TodoDetailViewModel(
    private val todoId: Long?,
    private val repository: TodoRepository,
    // Outlives the ViewModel: the write must not be cancelled when the screen is popped
    private val appScope: CoroutineScope,
) : ViewModel() {

    private val _state = MutableStateFlow<TodoDetailState>(
        if (todoId == null) TodoDetailState.Loaded(original = null) else TodoDetailState.Loading,
    )
    val state: StateFlow<TodoDetailState> = _state.asStateFlow()

    private var hasSaved = false

    init {
        if (todoId != null) {
            viewModelScope.launch {
                val todo = repository.getTodo(todoId)
                _state.value =
                    if (todo == null) TodoDetailState.NotFound else TodoDetailState.Loaded(todo)
            }
        }
    }

    /** Persists the edits (if any). Safe to call more than once; only the first call writes. */
    fun save(title: String, description: String) {
        val loaded = _state.value as? TodoDetailState.Loaded ?: return
        if (hasSaved) return
        hasSaved = true

        val original = loaded.original
        val trimmedTitle = title.trim()
        appScope.launch {
            if (original == null) {
                // A new to-do needs a title; otherwise it is discarded
                if (trimmedTitle.isNotEmpty()) repository.create(trimmedTitle, description)
            } else {
                // Blank title is not allowed: keep the previous one
                val newTitle = trimmedTitle.ifEmpty { original.title }
                if (newTitle != original.title || description != original.description) {
                    repository.updateContent(original.id, newTitle, description)
                }
            }
        }
    }
}

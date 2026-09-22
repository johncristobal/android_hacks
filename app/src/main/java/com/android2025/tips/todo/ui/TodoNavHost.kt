package com.android2025.tips.todo.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.android2025.tips.todo.ui.detail.TodoDetailRoute
import com.android2025.tips.todo.ui.list.TodoListRoute
import kotlinx.serialization.Serializable

@Serializable
data object TodoList : NavKey

/** [id] is null when creating a new to-do. */
@Serializable
data class TodoDetail(val id: Long? = null) : NavKey

@Composable
fun TodoNavHost() {
    val backStack = rememberNavBackStack(TodoList)
    // Guard against double taps popping the root and leaving an empty back stack
    val pop: () -> Unit = { if (backStack.size > 1) backStack.removeLastOrNull() }

    NavDisplay(
        backStack = backStack,
        onBack = pop,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<TodoList> {
                TodoListRoute(
                    onTodoClick = { id -> backStack.add(TodoDetail(id)) },
                    onAddClick = { backStack.add(TodoDetail(null)) },
                )
            }
            entry<TodoDetail> { key ->
                TodoDetailRoute(
                    todoId = key.id,
                    onNavigateBack = pop,
                )
            }
        },
    )
}

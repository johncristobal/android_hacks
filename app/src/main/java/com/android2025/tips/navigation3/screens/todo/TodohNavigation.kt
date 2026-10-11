package com.android2025.tips.navigation3.screens.todo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.android2025.tips.navigation3.navigation.Route
import com.android2025.tips.navigation3.screens.auth.LoginScreen
import com.android2025.tips.navigation3.screens.auth.RegisterScreen
import com.android2025.tips.navigation3.viewmodels.LoginViewModel
import com.android2025.tips.navigation3.viewmodels.RegisterViewModel
import com.android2025.tips.navigation3.viewmodels.SharedAuthViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun TodoNavigation(
    modifier: Modifier = Modifier,
) {
    val todoBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration{
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Todo.TodoList::class, Route.Todo.TodoList.serializer())
                    subclass(Route.Todo.TodoDetail::class, Route.Todo.TodoDetail.serializer())
                }
            }
        },
        Route.Todo.TodoList
    )

    NavDisplay(
        backStack = todoBackStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.Todo.TodoList> {
                TodoListScreen(
                    onTodoClick = {
                        todoBackStack.add(Route.Todo.TodoDetail(it))
                    }
                )
            }
            entry<Route.Todo.TodoDetail> { key ->
                TodoDetailScreen(
                    todo = key.todo
                )
            }
        }
    )
}
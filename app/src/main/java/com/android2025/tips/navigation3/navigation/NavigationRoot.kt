package com.android2025.tips.navigation3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.android2025.tips.navigation3.screens.TodoDetailScreen
import com.android2025.tips.navigation3.screens.TodoListScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/*
        Navigation3
        https://developer.android.com/jetpack/compose/navigation

        Routes
            - creamos routes para las pantallas, ojo con NavKey y Route
            - podemos ewnvair data, string, etc

        Creamos vistas con composables
        creamos viewmodels para estas vistas
            - recuerda stateFlow para enviar data
            - sharedflow para eventos: click, etc

        NavigationRoot - el bueno
            - ojo con las configs
            - polymorphic para multiplatform
            - entrydecoratos paraeliminar viewmodels
            - entryprovider para definir rutas
 */

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration{
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.TodoList::class, Route.TodoList.serializer())
                    subclass(Route.TodoDetail::class, Route.TodoDetail.serializer())
                }
            }
        },
        Route.TodoList
    )

    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator {
                false
            }
        ),
        backStack = backStack,
        entryProvider = { key ->
            when(key) {
                is Route.TodoList -> {
                    NavEntry(key) {
                        TodoListScreen(
                            onTodoClick = {
                                backStack.add(Route.TodoDetail(it))
                            }
                        )
                    }
                }
                is Route.TodoDetail -> {
                    NavEntry(key) {
                        TodoDetailScreen(
                            todo = key.todo
                        )
                    }
                }
                else -> error("Unknown route")
            }
        }
    )
}
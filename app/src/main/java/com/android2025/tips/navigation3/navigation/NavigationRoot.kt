package com.android2025.tips.navigation3.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.android2025.tips.navigation3.screens.auth.AuthNavigation
import com.android2025.tips.navigation3.screens.settings.ChangeSettingScreen
import com.android2025.tips.navigation3.screens.settings.SettingsScreen
import com.android2025.tips.navigation3.screens.todo.TodoDetailScreen
import com.android2025.tips.navigation3.screens.todo.TodoListScreen
import com.android2025.tips.navigation3.screens.todo.TodoNavigationBar

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavState(
        startRoute = Route.Auth,
        topLevelRoute = TOP_LEVEL_DESTINATIONS.keys
    )

    val navigator = remember {
        Navigator(navigationState)
    }

    val resultStore = rememberResultStore()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (navigationState.topLevelRoute != Route.Auth) {
                TodoNavigationBar(
                    selectedKey = navigationState.topLevelRoute,
                    onSelectedKey = {
                        navigator.navigate(it)
                    }
                )
            }
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onBack = {
                navigator.goBack()
            },
            entries = navigationState.toEntries(
                entryProvider = entryProvider {
                    entry<Route.Auth> {
                        AuthNavigation(
                            onLogin = {
                                navigator.navigate(Route.Todo.TodoList)
                            }
                        )
                    }
                    entry<Route.Todo.TodoList> {
                        TodoListScreen(
                            onTodoClick = {
                                navigator.navigate(Route.Todo.TodoDetail(it))
                            }
                        )
                    }
                    entry<Route.Todo.TodoFavs> {
                        TodoListScreen(
                            onTodoClick = {
                                navigator.navigate(Route.Todo.TodoDetail(it))
                            }
                        )
                    }
                    entry<Route.Todo.TodoDetail> { key ->
                        TodoDetailScreen(
                            todo = key.todo
                        )
                    }
                    entry<Route.Settings.SettingsHome> {
                        SettingsScreen(
                            resultStore = resultStore,
                            onChangeSettingClick = {
                                navigator.navigate(Route.Settings.ChangeSetting)
                            }
                        )
                    }
                    entry<Route.Settings.ChangeSetting> {
                        ChangeSettingScreen(
                            resultStore = resultStore,
                            onSave = {
                                navigator.goBack()
                            }
                        )
                    }
                }
            )
        )
    }
}
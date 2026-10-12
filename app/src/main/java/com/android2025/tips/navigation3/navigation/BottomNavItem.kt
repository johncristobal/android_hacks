package com.android2025.tips.navigation3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val icon: ImageVector,
    val title: String
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    Route.Todo.TodoList to BottomNavItem(
        icon = Icons.Outlined.CheckCircle,
        title = "Todo"
    ),
    Route.Todo.TodoFavs to BottomNavItem(
        icon = Icons.Outlined.Favorite,
        title = "Favs"
    ),
    Route.Settings.SettingsHome to BottomNavItem(
        icon = Icons.Default.Settings,
        title = "Settings"
    )
)

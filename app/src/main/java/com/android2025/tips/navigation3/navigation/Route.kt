package com.android2025.tips.navigation3.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object TodoList: Route, NavKey

    @Serializable
    data class TodoDetail(val todo: String): Route, NavKey
}
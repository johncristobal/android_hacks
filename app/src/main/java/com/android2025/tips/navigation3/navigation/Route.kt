package com.android2025.tips.navigation3.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Auth: Route, NavKey {

        @Serializable
        data object Login: NavKey

        @Serializable
        data object Register: NavKey
    }

    @Serializable
    data object Todo: Route, NavKey {
        @Serializable
        data object TodoList: Route, NavKey

        @Serializable
        data class TodoDetail(val todo: String): Route, NavKey
    }

}
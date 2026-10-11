package com.android2025.tips.navigation3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class NavigationState (
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute by topLevelRoute

    val stackInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

@Composable
fun rememberNavState(
    startRoute: NavKey,
    topLevelRoute: Set<NavKey>
): NavigationState {

    val topLevel = rememberSerializable(
        startRoute,
        topLevelRoute,
        configuration = serializers,
        serializer = MutableStateSerializer(PolymorphicSerializer(NavKey::class))
    ) {
        mutableStateOf(startRoute)
    }

    val allRoutes = topLevelRoute + startRoute
    val backStacks = allRoutes.associateWith { key ->
        rememberNavBackStack(
            configuration = serializers,
            key
        )
    }

    return remember(startRoute, topLevelRoute) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevel,
            backStacks = backStacks
        )
    }
}

val serializers = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Auth::class, Route.Auth.serializer())
            subclass(Route.Auth.Login::class, Route.Auth.Login.serializer())
            subclass(Route.Auth.Register::class, Route.Auth.Register.serializer())
            subclass(Route.Todo::class, Route.Todo.serializer())
            subclass(Route.Todo.TodoList::class, Route.Todo.TodoList.serializer())
            subclass(Route.Todo.TodoFavs::class, Route.Todo.TodoFavs.serializer())
            subclass(Route.Todo.TodoDetail::class, Route.Todo.TodoDetail.serializer())
            subclass(Route.Todo.Settings::class, Route.Todo.Settings.serializer())
        }
    }
}

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator()
        )

        rememberDecoratedNavEntries(
            backStack = stack,
            entryProvider = entryProvider,
            entryDecorators = decorators
        )
    }

    return stackInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
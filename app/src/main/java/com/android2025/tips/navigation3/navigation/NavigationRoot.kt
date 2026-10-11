package com.android2025.tips.navigation3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.android2025.tips.navigation3.screens.auth.AuthNavigation
import com.android2025.tips.navigation3.screens.todo.TodoNavigation
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
    NavDisplay
        - entrydecoratos paraeliminar viewmodels
        - entryprovider para definir rutas
 */

/*
    Nested Nav Graph & shared viewmodels
        Auth and Todo Route
        - in cada uno definimos rutas especificas para sus vistas
        authNavigation and TodoNavigation
        - definimos navegacion para auth y para todo
        - en auth, ejemplo de sharedviewmodel para copartir entre login y register
        - backStack para add o remove
        - OJO - definimos onclick en screen para que se maneje en la navigation

    Recuerda
        - entryDecorators para eliminar viewmodels
        - entryProvider para definir rutas, checa codigo
 */

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val rootBack = rememberNavBackStack(
        configuration = SavedStateConfiguration{
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Auth::class, Route.Auth.serializer())
                    subclass(Route.Todo::class, Route.Todo.serializer())
                }
            }
        },
        Route.Auth
    )

    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = rootBack,
        entryProvider = entryProvider {
            entry<Route.Auth> {
                AuthNavigation(
                    onLogin = {
                        rootBack.remove(Route.Auth)
                        rootBack.add(Route.Todo)
                    }
                )
            }
            entry<Route.Todo> {
                TodoNavigation()
            }
        }
    )
}
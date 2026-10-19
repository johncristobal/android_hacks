# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repo is

A personal Android learning/bootcamp sandbox (`rootProject.name = "hacks"`, package `com.android2025.tips`). It's a single `app` module containing unrelated demo areas rather than one cohesive product — check which demo area you're in before assuming conventions carry over between them.

## Common commands

Run from the repo root using the Gradle wrapper:

- Build debug APK: `./gradlew assembleDebug`
- Run unit tests (JVM, `app/src/test`): `./gradlew testDebugUnitTest`
- Run a single unit test class: `./gradlew testDebugUnitTest --tests "com.android2025.tips.todo.TodoListViewModelTest"`
- Run a single unit test method: `./gradlew testDebugUnitTest --tests "com.android2025.tips.todo.TodoListViewModelTest.emits todos and stops loading"`
- Run instrumented tests (needs a connected device/emulator, `app/src/androidTest`): `./gradlew connectedDebugAndroidTest`
- Run a single instrumented test class: `./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.android2025.tips.todo.TodoDaoTest`
- Lint: `./gradlew lint`

## Architecture

### Two entry points, one app

`AndroidManifest.xml` declares the activities sharing the app process (`TodoApp` as the `Application` class, which starts Koin DI):

- `MainActivity` — the current launcher activity (`MAIN`/`LAUNCHER`) and a scratchpad for whichever demo is being explored. Its `setContent` currently renders `NavigationRoot()` (the Navigation 3 demo); `widgetsMaterialDemo(...)` and `flowsBootcamp(...)` are still defined in the same file — when asked to "show" a widget or flow demo, swap the `setContent` body rather than adding new screens.
- `TodoActivity` — hosts the To-Do app. Exported, but its launcher intent-filter is commented out in the manifest; launch it with `adb shell am start -n com.android2025.tips/.todo.TodoActivity` or move the intent-filter back.
- `CorautineDemoActivity` — a separate coroutines demo activity (not exported).

### To-do app (`todo/`) — the one real layered app in this repo

This is the only feature built with production-style architecture; treat it as the template if extending the pattern elsewhere.

- **DI**: Koin, wired in `todo/di/TodoModule.kt` and started from `TodoApp`. Notable: a named `AppScope` (`CoroutineScope(SupervisorJob() + Dispatchers.Default)`) is provided separately from `viewModelScope` specifically so deletes can finish even after the detail screen is popped — see `TodoDetailViewModel`.
- **Data layer**: Room (`todo/data/TodoDatabase.kt`, `TodoDao.kt`, `TodoEntity.kt`) behind a `TodoRepository` interface implemented by `RoomTodoRepository`. Domain model (`todo/domain/Todo.kt`) is mapped to/from the Room entity in `TodoRepository.kt` — don't leak `TodoEntity` past the repository.
- **Room schema export**: `room.schemaDirectory` is set to `app/schemas` in `app/build.gradle.kts`, and exported JSON schemas are committed (`app/schemas/com.android2025.tips.todo.data.TodoDatabase/1.json`) for use by `MigrationTestHelper` in instrumented tests. Bump the `@Database(version = ...)` and add a migration when changing `TodoEntity`, and commit the newly exported schema file.
- **Navigation**: Navigation 3 (`androidx.navigation3`), not the older Navigation Compose — a simple single-stack usage, independent of the `navigation3/` demo below. `todo/ui/TodoNavHost.kt` defines `NavKey` routes (`TodoList`, `TodoDetail`) and manually manages a `NavBackStack` — there's a deliberate double-tap guard (`if (backStack.size > 1)`) to avoid popping past the root.
- **ViewModels**: Constructed via Koin (`viewModel { ... }` in `TodoModule.kt`), with `TodoDetailViewModel` taking a nullable `id` param for create-vs-edit — `null` means creating a new to-do.
- **Testing pattern**: `FakeTodoRepository` (in `app/src/test/.../todo/`) is the in-memory `TodoRepository` used by ViewModel unit tests; real Room behavior (ordering, migrations) is covered separately in `TodoDaoTest` under `androidTest` using `Room.inMemoryDatabaseBuilder`. ViewModel tests use `UnconfinedTestDispatcher` + `Dispatchers.setMain`/`resetMain` and Truth assertions (`com.google.common.truth.Truth.assertThat`), not JUnit's own asserts.

### Navigation 3 demo (`navigation3/`)

A separate exploration of advanced Navigation 3 patterns (auth flow → bottom tabs with per-tab back stacks). Screens and ViewModels are fakes — not wired to the `todo/` Room/Koin stack; ViewModels are created with plain `viewModel { ... }`.

- **Routes**: `navigation/Route.kt` is a `@Serializable sealed interface Route : NavKey` grouped into `Auth`, `Todo`, `Settings`. Every new route **must also be registered** in the polymorphic `serializers` `SavedStateConfiguration` in `NavigationState.kt`, or state save/restore will crash.
- **Multi-back-stack model**: `NavigationState` holds one `NavBackStack` per top-level route plus the start route (`Route.Auth`); `stackInUse` renders the start stack + the current tab's stack, and `toEntries()` applies the saveable-state and ViewModel-store decorators to each stack.
- **`Navigator`**: navigating to a key that owns a back stack switches tabs; any other key is pushed onto the current tab's stack. `goBack()` at a tab root returns to the start route.
- **Tabs**: `TOP_LEVEL_DESTINATIONS` in `BottomNavItem.kt` defines the bottom bar; `NavigationRoot` hides it while on `Route.Auth`.
- **Nested NavDisplay**: `screens/auth/AuthNavigation.kt` runs its own inner back stack (Login/Register), sharing a `SharedAuthViewModel` scoped outside the inner `NavDisplay` while per-screen VMs are scoped per entry.
- **Returning results**: `ResultStore` (`rememberResultStore()`, saveable) passes values back, e.g. `ChangeSettingScreen` → `SettingsScreen`.
- `screens/todo/TodohNavigation.kt` (`TodoNavigation`) is a leftover nested-NavDisplay version that is no longer called — `NavigationRoot` registers the todo entries directly.

### SOLID demos (`solid/`)

One file per principle, prefixed by its letter (`S_SingleResponsibility.kt`, `O_OpenClosed.kt`, ...). KDoc-heavy instructional code with "Android takeaway" notes that reference the to-do app's layering. Keep the letter-prefix naming if adding files.

### Widgets / Compose sampler (`widgets/`)

Files are prefixed `A`–`N` (`AFoundations.kt`, `BScaffold.kt`, `CTextInputs.kt`, ... `NAdvanced.kt`) to encode a deliberate learning order through Compose concepts (layout foundations → scaffold → text input → clickables → progress → cards → grids → visuals → animations → canvas → gestures → paging → advanced). Preserve this ordering/naming scheme if adding a new topic file rather than inventing a different convention. `utils/material3/` holds newer Material3-specific samples (buttons, sliders, floating toolbars, loading indicators) kept separate from the general `widgets/` set.

### Kotlin Flows bootcamp (`kotlin_flows/`)

`MainViewModel.kt` is instructional code exploring `StateFlow`/`SharedFlow`/flow operators (`combine`, `zip`, `merge`, `flatMapConcat`, etc.). Large blocks are intentionally commented out as alternates/scratch experiments rather than dead code to delete — check with the user before removing commented-out sections here, since they're often reference material for whichever concept is being demonstrated next.

## Dependency notes

- Version catalog is `gradle/libs.versions.toml`; most deps go through `libs.*`, but several (Coil, Accompanist Pager, a second Material3 alpha build, Firebase Firestore, Truth, Turbine, kotlinx-coroutines-test) are declared as raw coordinate strings directly in `app/build.gradle.kts` instead of the catalog — check both places when bumping versions.
- Navigation 3 (`navigation3-runtime`, `navigation3-ui`, `lifecycle-viewmodel-navigation3`) and `kotlinx-serialization-json` come from the catalog, with the `kotlin.serialization` plugin applied in `app/build.gradle.kts` (required for `@Serializable` routes). The catalog also has commented-out Compose Multiplatform Nav3 entries.
- DI is Koin (`io.insert-koin`), not Hilt/Dagger — new injectable dependencies for the to-do app go in `todo/di/TodoModule.kt`.
- `minSdk = 25`, `compileSdk`/`targetSdk = 37`, Kotlin `2.2.10`, AGP `9.2.1`.

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

`AndroidManifest.xml` declares two launchable activities sharing the app process (`TodoApp` as the `Application` class, which starts Koin DI):

- `TodoActivity` — the actual launcher activity (`MAIN`/`LAUNCHER`), hosts the To-Do app.
- `MainActivity` — a scratchpad activity (not exported as launcher) used to manually wire up whichever Compose widget/flow demo is being explored. Its `setContent` body is largely commented-out call sites (`widgetsMaterialDemo(...)`, `flowsBootcamp(...)`) — when asked to "show" a widget or flow demo, expect to uncomment/swap these rather than add new screens.
- `CorautineDemoActivity` — a separate coroutines demo activity.

### To-do app (`todo/`) — the one real layered app in this repo

This is the only feature built with production-style architecture; treat it as the template if extending the pattern elsewhere.

- **DI**: Koin, wired in `todo/di/TodoModule.kt` and started from `TodoApp`. Notable: a named `AppScope` (`CoroutineScope(SupervisorJob() + Dispatchers.Default)`) is provided separately from `viewModelScope` specifically so deletes can finish even after the detail screen is popped — see `TodoDetailViewModel`.
- **Data layer**: Room (`todo/data/TodoDatabase.kt`, `TodoDao.kt`, `TodoEntity.kt`) behind a `TodoRepository` interface implemented by `RoomTodoRepository`. Domain model (`todo/domain/Todo.kt`) is mapped to/from the Room entity in `TodoRepository.kt` — don't leak `TodoEntity` past the repository.
- **Room schema export**: `room.schemaDirectory` is set to `app/schemas` in `app/build.gradle.kts`, and exported JSON schemas are committed (`app/schemas/com.android2025.tips.todo.data.TodoDatabase/1.json`) for use by `MigrationTestHelper` in instrumented tests. Bump the `@Database(version = ...)` and add a migration when changing `TodoEntity`, and commit the newly exported schema file.
- **Navigation**: Navigation 3 (`androidx.navigation3`), not the older Navigation Compose. `todo/ui/TodoNavHost.kt` defines `NavKey` routes (`TodoList`, `TodoDetail`) and manually manages a `NavBackStack` — there's a deliberate double-tap guard (`if (backStack.size > 1)`) to avoid popping past the root.
- **ViewModels**: Constructed via Koin (`viewModel { ... }` in `TodoModule.kt`), with `TodoDetailViewModel` taking a nullable `id` param for create-vs-edit — `null` means creating a new to-do.
- **Testing pattern**: `FakeTodoRepository` (in `app/src/test/.../todo/`) is the in-memory `TodoRepository` used by ViewModel unit tests; real Room behavior (ordering, migrations) is covered separately in `TodoDaoTest` under `androidTest` using `Room.inMemoryDatabaseBuilder`. ViewModel tests use `UnconfinedTestDispatcher` + `Dispatchers.setMain`/`resetMain` and Truth assertions (`com.google.common.truth.Truth.assertThat`), not JUnit's own asserts.

### Widgets / Compose sampler (`widgets/`)

Files are prefixed `A`–`N` (`AFoundations.kt`, `BScaffold.kt`, `CTextInputs.kt`, ... `NAdvanced.kt`) to encode a deliberate learning order through Compose concepts (layout foundations → scaffold → text input → clickables → progress → cards → grids → visuals → animations → canvas → gestures → paging → advanced). Preserve this ordering/naming scheme if adding a new topic file rather than inventing a different convention. `utils/material3/` holds newer Material3-specific samples (buttons, sliders, floating toolbars, loading indicators) kept separate from the general `widgets/` set.

### Kotlin Flows bootcamp (`kotlin_flows/`)

`MainViewModel.kt` is instructional code exploring `StateFlow`/`SharedFlow`/flow operators (`combine`, `zip`, `merge`, `flatMapConcat`, etc.). Large blocks are intentionally commented out as alternates/scratch experiments rather than dead code to delete — check with the user before removing commented-out sections here, since they're often reference material for whichever concept is being demonstrated next.

## Dependency notes

- Version catalog is `gradle/libs.versions.toml`; most deps go through `libs.*`, but several (Coil, Accompanist Pager, a second Material3 alpha build, Firebase Firestore, Truth, Turbine, kotlinx-coroutines-test) are declared as raw coordinate strings directly in `app/build.gradle.kts` instead of the catalog — check both places when bumping versions.
- DI is Koin (`io.insert-koin`), not Hilt/Dagger — new injectable dependencies for the to-do app go in `todo/di/TodoModule.kt`.
- `minSdk = 25`, `compileSdk`/`targetSdk = 37`, Kotlin `2.2.10`, AGP `9.2.1`.

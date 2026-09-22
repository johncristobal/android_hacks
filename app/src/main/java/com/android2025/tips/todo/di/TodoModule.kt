package com.android2025.tips.todo.di

import androidx.room.Room
import com.android2025.tips.todo.data.RoomTodoRepository
import com.android2025.tips.todo.data.TodoDatabase
import com.android2025.tips.todo.data.TodoRepository
import com.android2025.tips.todo.ui.detail.TodoDetailViewModel
import com.android2025.tips.todo.ui.list.TodoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Scope that outlives any ViewModel; used for writes that must finish after a screen is popped. */
val AppScope = named("appScope")

val todoModule = module {
    single {
        Room.databaseBuilder(androidContext(), TodoDatabase::class.java, TodoDatabase.NAME).build()
    }
    single { get<TodoDatabase>().todoDao() }
    single<TodoRepository> { RoomTodoRepository(get()) }
    single<CoroutineScope>(AppScope) { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    viewModel { TodoListViewModel(get()) }
    viewModel { params -> TodoDetailViewModel(params.getOrNull<Long>(), get(), get(AppScope)) }
}

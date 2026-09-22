package com.android2025.tips.todo

import android.app.Application
import com.android2025.tips.todo.di.todoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TodoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TodoApp)
            modules(todoModule)
        }
    }
}

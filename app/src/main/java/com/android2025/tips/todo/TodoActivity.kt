package com.android2025.tips.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android2025.tips.todo.ui.TodoNavHost
import com.android2025.tips.ui.theme.HacksTheme

class TodoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HacksTheme {
                TodoNavHost()
            }
        }
    }
}

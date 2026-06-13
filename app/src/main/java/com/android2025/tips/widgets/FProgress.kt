package com.android2025.tips.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme
import kotlinx.coroutines.delay

@Composable
fun ProgressSample(modifier: Modifier = Modifier) {
    var progress by remember { mutableStateOf(0.2f) } // 20%

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
            CircularProgressIndicator(progress = progress)
            Spacer(modifier = Modifier.padding(8.dp))

            LinearProgressIndicator(progress = progress)
            Spacer(modifier = Modifier.padding(8.dp))

            LaunchedEffect(Unit) {
                while (progress < 1f) {
                    delay(100)
                    progress += 0.05f
                }
            }

            LinearProgressIndicator(progress = progress.coerceIn(0f, 1f))
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview
@Composable
fun PreviewProgress(modifier: Modifier = Modifier) {
    HacksTheme {
        ProgressSample()
    }
}
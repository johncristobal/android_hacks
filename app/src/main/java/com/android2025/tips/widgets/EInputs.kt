package com.android2025.tips.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme

@Composable
fun InputsSample(modifier: Modifier = Modifier) {
    var isChecked by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Other")
    var selected by remember { mutableStateOf(options[0]) }
    var isDarkMode by remember { mutableStateOf(false) }
    var volume by remember { mutableStateOf(0.5f) }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Checkbox sample
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text("Accept terms", modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // radios
            Column {
                options.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (it == selected),
                            onClick = { selected = it }
                        )
                        Text(text = it, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dark Mode")
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // slider
            Column {
                Text("Volume: ${(volume * 100).toInt()}%")
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    valueRange = 0f..1f,
                    steps = 4
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewInputs(modifier: Modifier = Modifier) {
    HacksTheme {
        InputsSample()
    }
}
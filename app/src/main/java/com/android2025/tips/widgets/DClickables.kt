package com.android2025.tips.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme

@Composable
fun ClickableSample(modifier: Modifier = Modifier) {

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            // Simple button
            Button(
                onClick = {}
            ) {
                Text("Submit")
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // OutlinedButton
            OutlinedButton(
                onClick = {}
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // icon button
            IconButton(
                onClick = {}
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "fav")
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // floating action button
            FloatingActionButton(
                onClick = { println("FAB clicked") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // click to any composable
            Text(
                text = "Tap me!",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { println("Text clicked!") }
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClickable(modifier: Modifier = Modifier) {
    HacksTheme {
        ClickableSample()
    }
}
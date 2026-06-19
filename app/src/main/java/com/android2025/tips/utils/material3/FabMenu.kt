package com.android2025.tips.utils.material3

import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.android2025.tips.R

@Composable
fun FabMenu(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = { expanded = it }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if(expanded) R.drawable.outline_close_24 else R.drawable.outline_battery_android_plus_24
                    ),
                    contentDescription = null ,
                    tint = if(expanded) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }
    ) {
        items.forEach { item ->
            FloatingActionButtonMenuItem(
                onClick = { expanded = false },
                text = { Text(item.text) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.icon),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

val items = listOf(
    FabItem("Item 1", R.drawable.outline_calendar_add_on_24),
    FabItem("Item 2", R.drawable.outline_arrow_drop_down_24),
    FabItem("Item 3", R.drawable.outline_bedtime_24),
)

data class FabItem(
    val text: String,
    val icon: Int
)

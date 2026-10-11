package com.android2025.tips.navigation3.screens.todo

import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.android2025.tips.navigation3.navigation.TOP_LEVEL_DESTINATIONS

@Composable
fun TodoNavigationBar(
    selectedKey: NavKey,
    onSelectedKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { (top, data) ->
            NavigationBarItem(
                selected = selectedKey == top,
                onClick = { onSelectedKey(top) },
                icon = {
                    if (data != null) {
                        Icon(
                            imageVector = data.icon,
                            contentDescription = data.title
                        )
                    }
                },
                label = {
                    if (data != null) {
                        Text(text = data.title)
                    }
                }
            )
        }
    }
}
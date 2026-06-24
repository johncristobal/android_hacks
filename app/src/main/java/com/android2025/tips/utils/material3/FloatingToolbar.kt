package com.android2025.tips.utils.material3

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.wear.compose.material3.OpenOnPhoneDialogDefaults.Icon
import com.android2025.tips.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingToolbar(modifier: Modifier = Modifier) {

    var expanded by remember {
        mutableStateOf(true)
    }

//    HorizontalFloatingToolbar(
    VerticalFloatingToolbar (
        modifier = modifier,
        expanded = expanded,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    expanded = !expanded
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.outline_arrow_drop_down_24,
                    ),
                    contentDescription = null,
                )
            }
        }
    ) {
        listOf(
            R.drawable.outline_close_24,
            R.drawable.outline_bedtime_24,
            R.drawable.outline_battery_android_plus_24,
            R.drawable.outline_close_24
        ).forEach { iconRes ->
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        iconRes
                    ),
                    contentDescription = null,
                )
            }
        }
    }
}

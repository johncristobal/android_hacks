package com.android2025.tips.navigation3.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android2025.tips.navigation3.navigation.ResultStore

@Composable
fun SettingsScreen(
    resultStore: ResultStore,
    onChangeSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val setting = resultStore.getResult<String>("main_setting")
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onChangeSettingClick
        ) {
            Text("Current setting: ${setting ?: "Default"}")
        }
    }
}
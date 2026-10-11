package com.android2025.tips.navigation3.screens.auth

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android2025.tips.navigation3.viewmodels.LoginViewModel
import com.android2025.tips.navigation3.viewmodels.RegisterViewModel
import com.android2025.tips.navigation3.viewmodels.SharedAuthViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    sharedViewModel: SharedAuthViewModel,
) {

    val localC by viewModel.counter.collectAsStateWithLifecycle()
    val sharedC by sharedViewModel.counter.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick =  { sharedViewModel.bump() }
        ) {
            Text("Shared counter $sharedC")
        }

        Button(
            onClick =  { viewModel.bump() }
        ) {
            Text("Local counter $localC")
        }
    }
}
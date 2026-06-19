package com.android2025.tips.utils.material3

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingIndicators(modifier: Modifier = Modifier) {
    CircularProgressIndicator()
    LoadingIndicator()
    ContainedLoadingIndicator()

    // progress indicators
    val transition = rememberInfiniteTransition()
    val prgoress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000)
        )
    )

    // interesante, usas unos sin progres para empezar conexion
    // y luegi con progress para ir cargando
    LinearProgressIndicator()
    LinearProgressIndicator(
        progress = { prgoress }
    )

    LinearWavyProgressIndicator(
        progress = { prgoress }
    )

    CircularProgressIndicator(
        progress = { prgoress }
    )

    CircularWavyProgressIndicator(
        progress = { prgoress }
    )
}
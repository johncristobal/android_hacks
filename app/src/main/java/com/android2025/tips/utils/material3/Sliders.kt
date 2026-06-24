package com.android2025.tips.utils.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalSlider
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Sliders(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0.5f) }
    val sliderState = rememberSliderState(
        value = 0.5f
    )
    var valueRange by remember {
        mutableStateOf(0.4f..0.6f)
    }

    Slider(
        state = sliderState,
    )

    RangeSlider(
//        state = sliderState,
        value = valueRange,
        onValueChange = { valueRange = it }
//        thumb = {
//            Text("Drag")
//        },
//        track = {
//            Box(
//                modifier = Modifier
//                    .background(Color.Red)
//            )
//        }
    )

    VerticalSlider(
        state = sliderState,
        reverseDirection = true
    )
}
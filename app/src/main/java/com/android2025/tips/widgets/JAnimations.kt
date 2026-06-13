package com.android2025.tips.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme
import kotlinx.coroutines.delay

@Composable
fun AnimationsSample(modifier: Modifier = Modifier) {
    var visible by remember { mutableStateOf(true) }
    var count by remember { mutableStateOf(0) }
    var rotation by remember { mutableStateOf(0f) }
    val radius = remember { Animatable(0f) }

    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            // animate smooth
            Column {
                Button(onClick = { visible = !visible }) {
                    Text(if (visible) "Hide" else "Show")
                }

                AnimatedVisibility(visible) {
                    Text("This text animates in/out")
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // transition between states
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { count++ }) {
                    Text("Increase")
                }

                AnimatedContent(targetState = count) { targetCount ->
                    Text("Count: $targetCount")
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // rotate, scale, translate
            LaunchedEffect(Unit) {
                while (true) {
                    delay(16)
                    rotation += 2f
                }
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer(rotationZ = rotation)
                    .background(Color.Green)
            )
            Spacer(modifier = Modifier.padding(8.dp))

            // custom background
            Box(
                Modifier
                    .size(120.dp)
                    .drawBehind {
                        drawCircle(Color.Red, radius = size.minDimension / 2)
                    }
            ) {
                Text("Inside Circle", modifier = Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // reveal custom
            LaunchedEffect(Unit) {
                radius.animateTo(500f, tween(1000))
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                clipPath(Path().apply {
                    addOval(Rect(center = Offset(size.width / 2, size.height / 2), radius = radius.value))
                }) {
                    drawRect(Color.Cyan)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAnimation() {
    HacksTheme {
        AnimationsSample()
    }
}
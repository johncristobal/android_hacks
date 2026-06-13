package com.android2025.tips.widgets

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.android2025.tips.ui.theme.HacksTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun GesturesSamples(modifier: Modifier = Modifier) {

    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, rotationChange, offsetChange ->
        scale *= zoomChange
        rotation += offsetChange
        offset += rotationChange
    }

    val width = 300f
    val swipeState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, width to 1)

    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            // tap and long press
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                println("Tapped at $offset")
                            },
                            onLongPress = {
                                println("Long pressed!")
                            }
                        )
                    }
            ) {
                Text("Tap or Long Press", modifier = Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // pinch to zoom
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .background(Color.Cyan)
                    .transformable(state)
            ) {
                Text("Pinch, Drag, Rotate", modifier = Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // swipe sample
            Box(
                modifier = Modifier
                    .width(width.dp)
                    .height(100.dp)
                    .background(Color.LightGray)
                    .swipeable(
                        state = swipeState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
            ) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
                        .fillMaxSize()
                        .background(Color.Green)
                ) {
                    Text("Swipe Me", modifier = Modifier.align(Alignment.Center))
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // touch events
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.Yellow)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                println("Finger down at: ${it.x}, ${it.y}")
                                true
                            }

                            MotionEvent.ACTION_UP -> {
                                println("Finger up!")
                                true
                            }

                            else -> false
                        }
                    }
            ) {
                Text("Interop Touch", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGestures() {
    HacksTheme {
        GesturesSamples()
    }
}
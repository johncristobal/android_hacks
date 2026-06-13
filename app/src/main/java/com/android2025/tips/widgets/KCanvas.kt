package com.android2025.tips.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme

@Composable
fun CanvasSample(modifier: Modifier = Modifier) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            // Great for shapes, paths, charts, and freehand UIs.
            Canvas(modifier = Modifier.size(100.dp)) {
                drawCircle(
                    color = Color.Blue,
                    radius = size.minDimension / 1,
                    center = Offset(0f,0f)
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // withcache, para semistatic custom UI
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .drawWithCache {
                        val gradient = Brush.radialGradient(
                            colors = listOf(Color.Yellow, Color.Red),
                            center = Offset(0f,0f),
                            radius = size.minDimension / 1
                        )
                        onDrawBehind {
                            drawRect(gradient)
                        }
                    }
            )
            Spacer(modifier = Modifier.padding(8.dp))

            // custom painter, define tus formas
//            Canvas(
//                modifier = Modifier
//                    .size(200.dp)
//            ) {
//                with(MyPainter()) {
//                    draw(this@Canvas)
//                }
//            }

            // dibuja a tu modo
            Canvas(modifier = Modifier
                .size(200.dp)
                .background(Color.Gray)
            ) {
                val path = Path().apply {
                    moveTo(0f, size.height / 2)
                    quadraticTo(
                        size.width / 2, 0f,
                        size.width, size.height / 2
                    )
                }

                drawPath(path, color = Color.Magenta, style = Stroke(width = 5f))
            }
            Spacer(modifier = Modifier.padding(8.dp))

            // combina para otros efectos
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .graphicsLayer(rotationZ = 45f, scaleX = 2.2f)
                    .background(Color.Green)
            )
        }
    }
}

class MyPainter : Painter() {
    override val intrinsicSize: Size
        get() = Size.Unspecified

    override fun DrawScope.onDraw() {
        drawLine(Color.Black, Offset.Zero, Offset(size.width, size.height), strokeWidth = 4f)
    }
}

@Preview
@Composable
private fun CanvasPreview() {
    HacksTheme {
        CanvasSample()
    }
}
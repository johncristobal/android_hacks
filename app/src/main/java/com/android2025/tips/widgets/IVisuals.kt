package com.android2025.tips.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.placeholder
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.android2025.tips.R
import com.android2025.tips.ui.theme.HacksTheme

@Composable
fun VisualsExample(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val imageUrl = "https://picsum.photos/400"

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hospital_icon),
                contentDescription = "sample",
                modifier = Modifier.size(100.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
//                    placeholder = painterResource(R.drawable.placeholder),
//                    error = painterResource(R.drawable.error),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star Icon",
                tint = Color.Yellow,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text("Above Divider")
                Divider(color = Color.Gray, thickness = 1.dp)
                Text("Below Divider")
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Cyan)
            ) {
                Text("Rounded", modifier = Modifier.align(Alignment.Center))
            }

            Column {
                Text("Top Text")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Bottom Text")
            }

            Text(
                text = "",
                modifier = Modifier
                    .size(100.dp)
//                    .placeholder()
            )

            BadgedBox(badge = { Badge { Text("3") } }) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewVisual() {
    HacksTheme {
        VisualsExample()
    }
}
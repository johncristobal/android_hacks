package com.android2025.tips.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme


@Composable
fun GridsSample(modifier: Modifier = Modifier) {
    val items = (1..20).map { "Item $it" }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Text(items[it])
                }
            }
        }
    }
}

@Composable
fun CustomStaggeredGrid(items: List<String>) {
    val colum1 = items.filterIndexed { index, _ -> index % 2 == 0 }
    val colum2 = items.filterIndexed { index, _ -> index % 2 != 0 }

    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                items(colum1) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(it.toString(), modifier = Modifier.padding(16.dp))
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                items(colum2) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(it.toString(), modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

// Great example to show chips
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowExample() {
    val tags = listOf("Kotlin", "Compose", "UI", "Grid", "Responsive", "Material")

    FlowRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.LightGray
            ) {
                Text(tag, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewGrids(modifier: Modifier = Modifier) {
    HacksTheme {
        val items = (1..20).map { "Item $it" }
//        GridsSample()
//        CustomStaggeredGrid(items)
        FlowRowExample()
    }
}
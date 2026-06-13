package com.android2025.tips.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme
import kotlin.math.roundToInt

@Composable
fun AdavancedExamples(modifier: Modifier = Modifier) {
    val tabs = listOf("Home", "Trending", "Subscriptions", "Library", "History")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val allSuggestions = listOf("Apple", "Apricot", "Banana", "Blueberry", "Cherry")
    var text by remember { mutableStateOf("") }

    val filteredSuggestions = allSuggestions.filter {
        it.startsWith(text, ignoreCase = true) && text.isNotBlank()
    }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            // Set a lot of tabs in one row
            ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            Spacer(modifier = Modifier
                .padding(8.dp))

            // auto complete text field / custom design
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Fruit") }
                )

                filteredSuggestions.forEach {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { text = it }
                            .padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier
                .padding(8.dp))


        }
    }
}

@Composable
fun NestedScrollExample() {
    val toolbarHeight = 200.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }
    val offsetY = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = (offsetY.value + delta).coerceIn(-toolbarHeightPx, 0f)
                offsetY.value = newOffset
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Text("Collapsing Toolbar", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn {
            items(50) {
                ListItem(headlineContent = { Text("Item #$it") })
            }
        }
    }
}

@Preview
@Composable
private fun AdvancedPreview() {
    HacksTheme {
        NestedScrollExample()
    }
}
package com.android2025.tips.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android2025.tips.ui.theme.HacksTheme
import com.google.accompanist.pager.ExperimentalPagerApi
//import com.google.accompanist.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
    * Good examples for pager, tabs
 */

@Composable
fun PagingSamples(modifier: Modifier = Modifier) {
    Scaffold {
        Column(modifier = Modifier
            .padding(it)) {

            val pagerState = rememberPagerState(pageCount = { 5 })

            Column {
                HorizontalPager(state = pagerState) { page ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xFFBBDEFB + page * 1000))
                    ) {
                        Text("Page $page", Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalPagerExample() {
    val pagerState = rememberPagerState(pageCount = { 5 })

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    when (page % 3) {
                        0 -> Color(0xFFE3F2FD)
                        1 -> Color(0xFFC8E6C9)
                        else -> Color(0xFFFFF9C4)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Page $page", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerWithTabs() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val tabTitles = listOf("Home", "Profile", "Settings")

    Column {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { CoroutineScope(Dispatchers.Main).launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(state = pagerState) { page ->
            Box(Modifier.fillMaxSize().padding(16.dp)) {
                Text("Content for ${tabTitles[page]}")
            }
        }
    }
}

@Composable
fun SnapperLazyRowExample() {
    val items = listOf("One", "Two", "Three", "Four", "Five")

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 32.dp),
//        flingBehavior = rememberSnapperFlingBehavior()
        // 👆 This is the key line that adds snapping behavior
    ) {
        items(items) { item ->
            Box(
                modifier = Modifier
                    .size(140.dp, 100.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview
@Composable
private fun PagingPreview() {
    HacksTheme {
        SnapperLazyRowExample()
    }
}
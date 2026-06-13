package com.android2025.tips.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme

// define un cuadro y los elementos se alinean dentro del mismo
@Composable
fun BoxExample() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.LightGray)
    ) {
        Text(
            "Bottom",
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            "Top",
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

// recycler view mucho mas facil
@Composable
fun LazyExample(modifier: Modifier = Modifier) {
    LazyColumn(
//    LazyRow(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(10) {
            Text("Item $it", modifier = Modifier.padding(8.dp))
        }
    }
}

//@Composable
//fun ConstraintExample(modifier: Modifier = Modifier) {
//    ConstraintLayout(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        val (text1, text2) = createRefs()
//
//        Text("start", Modifier.constrainAs(text1))
//    }
//}



@Preview
@Composable
fun Preview(){
    HacksTheme {
        Surface {
//            BoxExample()
            LazyExample()
        }
    }
}
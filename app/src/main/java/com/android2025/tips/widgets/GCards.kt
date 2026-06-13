package com.android2025.tips.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme

@Composable
fun CardSample(modifier: Modifier = Modifier) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        ) {
            // card sample
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Card Title", style = MaterialTheme.typography.titleMedium)
                    Text("This is a card with rounded corners and elevation.")
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))

            //Use Surface for building custom cards,
            // interactive sections,
            // or themable backgrounds.
            Surface(
                color = Color(0xFFE0F7FA),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "This is inside a Surface container",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))

//            val backdropState =rememberBackdropScaffoldState(BackdropValue.Concealed)
//            val scope = rememberCoroutineScope()
//
//            BackdropScaffold(
//                scaffoldState = backdropState,
//                backLayerContent = {
//                    Text(
//                        "Back Layer - Filters or Categories",
//                        modifier = Modifier.padding(16.dp)
//                    )
//                },
//                frontLayerContent = {
//                    Text(
//                        "Front Layer - Main Content",
//                        modifier = Modifier.padding(16.dp)
//                    )
//                },
//                appBar = {
//                    TopAppBar(
//                        title = { Text("Backdrop Scaffold") },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    if (backdropState.isConcealed) {
//                                        backdropState.reveal()
//                                    } else {
//                                        backdropState.conceal()
//                                    }
//                                }
//                            }) {
//                                Icon(Icons.Default.Menu, contentDescription =
//                                    "Toggle Back Layer")
//                            }
//                        }
//                    )
//                }
//            )
        }
    }
}

@Preview
@Composable
fun PreviewCards(modifier: Modifier = Modifier) {
    HacksTheme {
        CardSample()
    }
}
package com.android2025.tips.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android2025.tips.ui.theme.HacksTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample(modifier: Modifier = Modifier) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)) {
                Text("Drawer Item 1")
                Text("Drawer Item 2")
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6200EE), // Background color
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    title = {
                        Text("Sample")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "menu")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "search")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color(0xFF03DAC5),
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "home")
                    }
                }
            },
        ) {
            Column(modifier = Modifier.padding(it)) {
                Text("CONTENT", modifier = Modifier.padding(16.dp))
                Button(onClick = { scope.launch { drawerState.open() } }) {
                    Text("Open Drawer")
                }
            }
        }
    }
}

@Composable
fun DrawerExample(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
            ) {
                Text("Drawer Item 1")
                Text("Drawer Item 2")
            }
        }
    ) {
        Button(onClick = { scope.launch { drawerState.open() } }) {
            Text("Open Drawer")
        }
    }
}

// material2 bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScaffoldExample() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Bottom Sheet Content")
            }
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            TopAppBar(title = { Text("BottomSheetScaffold") })
        }
    ) {
        Button(onClick = {
            scope.launch { scaffoldState.bottomSheetState.expand() }
        }) {
            Text("Show Bottom Sheet")
        }
    }
}

// material3 sample bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetExample() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var openSheet by remember { mutableStateOf(false) }

    if (openSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                openSheet = false
                scope.launch { sheetState.hide() }
            },
            sheetState = sheetState,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Sheet Item 1")
                Text("Sheet Item 2")
                Text("Sheet Item 3")
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .padding(it)) {
            Text("Main content here")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                openSheet = true
                scope.launch { sheetState.show() }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Open Sheet")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Open Sheet")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { scope.launch { sheetState.hide() } }) {
                Text("Hide Bottom Sheet")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScaffold(){
    HacksTheme {
        Surface {
//        ScaffoldExample()
//        DrawerExample()
//        BottomSheetScaffoldExample()
            ModalBottomSheetExample()
        }
    }
}
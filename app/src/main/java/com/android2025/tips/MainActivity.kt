package com.android2025.tips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import com.android2025.tips.ui.theme.HacksTheme
import com.android2025.tips.utils.material3.FabMenu
import com.android2025.tips.utils.material3.FloatingToolbar
import com.android2025.tips.utils.material3.LoadingIndicators
import com.android2025.tips.utils.material3.MultiplechoiceButtonGroup
import com.android2025.tips.utils.material3.SinglechoiceButtonGroup
import com.android2025.tips.utils.material3.Sliders
import com.android2025.tips.utils.material3.SplitButtons
import com.android2025.tips.widgets.AnimationsSample
import com.android2025.tips.widgets.GesturesSamples
import com.android2025.tips.widgets.ModalBottomSheetExample
import com.android2025.tips.widgets.ScaffoldExample
import com.android2025.tips.widgets.VisualsExample
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HacksTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FabMenu()
                    }
                ) { innerPadding ->
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        SplitButtons()
                        SinglechoiceButtonGroup()
                        MultiplechoiceButtonGroup()
                        LoadingIndicators()
                        Sliders()
                        FloatingToolbar()
                        FloatingToolbar()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HacksTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Sliders()
        }
    }
}
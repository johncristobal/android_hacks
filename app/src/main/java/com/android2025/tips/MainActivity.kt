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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import com.android2025.tips.ui.theme.HacksTheme
import com.android2025.tips.utils.material3.MultiplechoiceButtonGroup
import com.android2025.tips.utils.material3.SinglechoiceButtonGroup
import com.android2025.tips.utils.material3.SplitButtons
import com.android2025.tips.widgets.AnimationsSample
import com.android2025.tips.widgets.GesturesSamples
import com.android2025.tips.widgets.ModalBottomSheetExample
import com.android2025.tips.widgets.ScaffoldExample
import com.android2025.tips.widgets.VisualsExample

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HacksTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
//                        SplitButtons()
                        SinglechoiceButtonGroup()
                        MultiplechoiceButtonGroup()
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
//        SinglechoiceButtonGroup()
            MultiplechoiceButtonGroup()
        }
    }
}
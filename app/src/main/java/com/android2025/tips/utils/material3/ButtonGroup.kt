package com.android2025.tips.utils.material3

import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.Text
import com.android2025.tips.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SinglechoiceButtonGroup(modifier: Modifier = Modifier) {
    var selected by remember {
        mutableIntStateOf(0)
    }

    ButtonGroup(
        modifier = modifier,
        overflowIndicator = {
            FilledTonalIconButton(
                onClick = {
                    it.show()
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_menu_close_24),
                    contentDescription = null
                )
            }
        },
    ) {
        for (i in 0 until 5) {
            val checked = i == selected
            this.toggleableItem(
                checked = checked,
                label = "item $i",
                onCheckedChange = { if (it) selected = i },
                icon = if (checked) {
                    {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_bedtime_24),
                            contentDescription = null
                        )
                    }
                } else null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MultiplechoiceButtonGroup(modifier: Modifier = Modifier) {
    val indices = remember {
        mutableStateListOf<Int>()
    }

    ButtonGroup(
        modifier = modifier,
        overflowIndicator = {
            FilledTonalIconButton(
                onClick = {
                    it.show()
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_menu_close_24),
                    contentDescription = null
                )
            }
        },
    ) {
        for (i in 0 until 5) {
            val checked = i in indices
            this.toggleableItem(
                checked = checked,
                label = "item $i",
                onCheckedChange = {
                    if (it) {
                        indices.add(i)
                    } else {
                        indices.remove(i)
                    }
                },
                icon = if (checked) {
                    {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_bedtime_24),
                            contentDescription = null
                        )
                    }
                } else null
            )
        }
    }
}
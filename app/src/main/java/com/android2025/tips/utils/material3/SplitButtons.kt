package com.android2025.tips.utils.material3

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.android2025.tips.R
import kotlin.math.tan

@Composable
fun SplitButtons(modifier: Modifier = Modifier) {
    SplitButtonLayout(
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_edit_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(SplitButtonDefaults.LeadingIconSize)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit"
                )
            }
        },
        trailingButton = {
            var checked by remember {
                mutableStateOf(false)
            }
            val rotation by animateFloatAsState(
                targetValue = if (checked) 180f else 0f
            )
            
            SplitButtonDefaults.TrailingButton(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_drop_down_24),
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                )
            }
            DropdownMenu(
                expanded = checked,
                onDismissRequest = {
                    checked = false
                }
            ) {
                for(i in 1..3) {
                    DropdownMenuItem(
                        text = { Text("Item $i") },
                        onClick = {
                            checked = false
                        }
                    )
                }
            }
        }
    )
}
package com.android2025.tips.utils.material3

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.android2025.tips.R

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
                    contentDescription = null
                )
                Text(
                    text = "Edit"
                )
            }
        },
        trailingButton = {

        }
    )
}
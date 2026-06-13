package com.android2025.tips.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android2025.tips.ui.theme.HacksTheme

@Composable
fun SimpleTexts(modifier: Modifier = Modifier) {

    var text by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Simple text
                Text(
                    "Hellou",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // TextField basic
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text("Enter name")
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // outlined textfield
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email")
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // clicklabel ytext
                val annotatedText = buildAnnotatedString {
                    append("By clicking, you agree to the ")
                    pushStringAnnotation(tag = "TERMS", annotation = "https://example.com/terms")
                    withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                        append("Terms & Conditions")
                    }
                    pop()
                }

                ClickableText (
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations("TERMS", offset, offset)
                            .firstOrNull()?.let {
                                println("clicked: ${it.item}")
                            }
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))

                //allow copy and paste
                SelectionContainer {
                    Text("You can select and copy this text.")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewText(modifier: Modifier = Modifier) {
    HacksTheme {
        SimpleTexts()
    }
}
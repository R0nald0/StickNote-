package com.example.lembretes.presentation.ui.shared.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.lembretes.presentation.ui.theme.LembretesTheme

@Composable
fun StickNoteTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    enable: Boolean = true,
    isError: Boolean,
    onChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    maxLines: Int = 1,
    singleLine: Boolean,
    icon: @Composable() (() -> Unit)?,
    trailingIcon: @Composable() (() -> Unit)?,
    supportTexting: @Composable() (() -> Unit)?,
) {
    var erroText by remember {
        mutableStateOf(supportTexting)
    }

    OutlinedTextField(
        maxLines = maxLines,
        value = value,
        enabled = enable,
        singleLine = singleLine,
        isError = if (value.isEmpty()) {
            isError
        } else false,
        modifier = modifier.fillMaxWidth(),
        prefix = icon,
        supportingText = if (value.isEmpty()) erroText else null,
        trailingIcon = {
            if (isError) {
                trailingIcon
            }
        },
        label = {
            Text(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = label,
                style = textStyle
            )
        },
        onValueChange = onChange


    )
}

@Preview
@Composable
private fun StickNoteTextFieldPreview() {
    LembretesTheme {
        StickNoteTextField(
            value = "Teste",
            label = "Teste",
            isError = false,
            onChange = {},
            maxLines = 1,
            singleLine = true,
            icon = {
                Icon(Icons.Default.Person, contentDescription = "")
            },
            trailingIcon = {
                Icon(Icons.Default.Info, contentDescription = "", tint = Color.Red)
            },
            supportTexting = {}

        )
    }
}
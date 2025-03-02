package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteTextField
import com.example.lembretes.presentation.ui.theme.LembretesTheme

@Composable
fun StickNoteTagArea(
    modifier: Modifier = Modifier,
    label: String,
    limitCha: Int,
    tags: List<String>,
    tag: String,
    onAdd: () -> Unit,
    onRemove: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StickNoteTextField(
                modifier = Modifier.fillMaxWidth(0.6f),
                value = tag,
                label = label,
                isError = false,
                enable = tags.size < 2,
                maxLines = 1,
                onChange = onTextChange,
                supportTexting ={
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "${tag.length}/$limitCha")
                    }
                },
                singleLine = true,
                icon = { },
                trailingIcon = {}
            )
            IconButton(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    shape = CircleShape
                ),
                onClick = onAdd
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
        ) {
            tags.forEach {
                InputChip(
                    selected = true,
                    trailingIcon = { Icon(Icons.Filled.Close, contentDescription = "") },
                    onClick = { onRemove(it) },
                    label = {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                        )
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview
@Composable
private fun StickNoteSurFaceHasTagAreaPrev() {
    LembretesTheme {
        StickNoteTagArea(
            limitCha = 3,
            label = "Tag",
            tags = listOf("#tes", "#tag"),
            tag = "",
            onTextChange = {},
            onRemove = {},
            onAdd = {}
        )
    }
}
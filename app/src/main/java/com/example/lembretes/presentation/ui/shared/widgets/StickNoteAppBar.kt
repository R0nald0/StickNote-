package com.example.lembretes.presentation.ui.shared.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickNoteAppBar(
    modifier: Modifier = Modifier,
    onClosed:()->Unit,
    title :  String,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onClosed,
                content = {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary
            )
        },
    )
}
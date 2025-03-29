package com.example.lembretes.core.notification

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StickNoteSnackBar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    colorContainer : Color =  MaterialTheme.colorScheme.onPrimaryContainer
) {

    SnackbarHost(hostState = snackbarHostState){data ->
        Snackbar (
           modifier =  modifier,
            snackbarData = data,
            containerColor = colorContainer
        )
    }
}

@Composable
fun StickNoteSnackBarInfo(
    message : String,
    colorContainer : Color =  MaterialTheme.colorScheme.onPrimaryContainer,
    onAction: ()-> Unit
) {

   Snackbar(
       action =onAction,
       containerColor = colorContainer
   ) {
       Text(message)
   }
}

package com.example.lembretes.core.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class SnackBarEnumType{
    ERROR,INFO
}

@Composable
fun StickNoteSnackBar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackBarEnumType: SnackBarEnumType
) {

    var colorBackground by remember { mutableStateOf(Color.Red) }
    var colorIcon by remember { mutableStateOf(Color.Red) }

     when(snackBarEnumType){
        SnackBarEnumType.ERROR -> {
          colorBackground = MaterialTheme.colorScheme.error
            colorIcon = MaterialTheme.colorScheme.onSecondary
        }
        SnackBarEnumType.INFO -> {
            colorBackground = MaterialTheme.colorScheme.onPrimaryContainer
            colorIcon = MaterialTheme.colorScheme.primary
        }
    }

    SnackbarHost(
        modifier = modifier.padding(16.dp).clip(shape = RoundedCornerShape(10)),
        hostState = snackbarHostState,
        ){data ->
        Snackbar (
            modifier =  modifier,
            containerColor = colorBackground
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(data.visuals.message)
                Icon(Icons.Filled.Info, tint = colorIcon, contentDescription = "Info icon")
            }
        }

    }
}

@Composable
fun StickNoteSnackBarInfo(
    message : String,
    colorContainer : Color =  MaterialTheme.colorScheme.onPrimaryContainer,
    onAction: ()-> Unit
) {

   Snackbar(
       action = onAction,
       containerColor = colorContainer
   ) {
       Text(message)
   }
}

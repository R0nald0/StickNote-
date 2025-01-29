package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarWidget(
    datePickerState : DatePickerState,
    modifier: Modifier = Modifier,
    onDissmis: () -> Unit,
    onClick: () -> Unit
) {
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDissmis,
        confirmButton = {
            TextButton(
                onClick = onClick
            ){
                Text(text = "ok")
            }

        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CalendarWidgetPrev() {
  val cal  = Calendar.getInstance()
    LembretesTheme {
        CalendarWidget(
            datePickerState = rememberDatePickerState(),
            onDissmis = {},
            onClick = {},
        )
    }
}
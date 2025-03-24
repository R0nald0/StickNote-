package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarWidget(
    modifier: Modifier = Modifier,
    onDissmis: () -> Unit,
    onClick: (Long?) -> Unit
) {

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(timeZone = TimeZone.UTC).date
                return  selectedDate >= Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date
            }
        }
    )

    DatePickerDialog(
        modifier = modifier,
        colors = DatePickerDefaults.colors(
            dayContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onDismissRequest = onDissmis,
        confirmButton = {
            TextButton(
                onClick = {
                    onClick(datePickerState.selectedDateMillis)
                }
            ){
                Text(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    text = "ok"
                )
            }

        }
    ) {
        DatePicker(
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedDayContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            state = datePickerState,
        )
    }
}


@Preview
@Composable
private fun CalendarWidgetPrev() {
    LembretesTheme {
        CalendarWidget(
            onDissmis = {},
            onClick = {},
        )
    }
}
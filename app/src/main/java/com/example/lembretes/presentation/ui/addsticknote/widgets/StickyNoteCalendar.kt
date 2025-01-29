package com.example.lembretes.presentation.ui.addsticknote.widgets

import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.utils.convertDateLongToString
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickyNoteCalendar(
    modifier: Modifier = Modifier,
    onSelectedDate :(Long?)->Unit,
    dateResult : String,
    datePickerState : DatePickerState
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            showDatePicker = true
        }) {
        Text(
            text = dateResult,
            color = MaterialTheme.colorScheme.primary,
        )
    }

    if(showDatePicker){
        CalendarWidget(
            datePickerState =datePickerState,
            modifier = modifier,
            onClick = {
                showDatePicker = false
                val selcc  =  datePickerState.selectedDateMillis
                onSelectedDate(selcc)
            },
            onDissmis = {showDatePicker = false}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun StickyNoteCalendarPrev() {
    val datePickerState  = rememberDatePickerState()
    LembretesTheme {
        StickyNoteCalendar(
            dateResult = "12/04/2010",
            datePickerState = datePickerState,
            onSelectedDate = {}
        )
    }
}
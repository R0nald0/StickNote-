package com.example.lembretes.presentation.ui.addsticknote.widgets

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun StickyNoteCalendar(
    isError: Pair<Boolean, String?>,
    modifier: Modifier = Modifier,
    onSelectedDate: (Long?) -> Unit,
    date: String?
) {

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimerPicker by remember { mutableStateOf(false) }
    var dataResult by remember { mutableStateOf(date ?: "Escolha uma Data") }
    var dateChosed by remember { mutableLongStateOf(0L) }

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        border = if (isError.first) BorderStroke(1.dp, color = Color.Red)
        else BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
        onClick = {
            showDatePicker = true
        }) {
        Text(
            text = dataResult,
            color = if (!isError.first) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error,
        )
    }

    if (showDatePicker) {
        AnimatedVisibility(showDatePicker) {
            CalendarWidget(
                modifier = modifier,
                onClick = { date ->
                    showDatePicker = false
                    dataResult = date?.let {
                        dateChosed = date
                        ""
                    } ?: "Escolha uma Data"

                    if (date != null) {
                        showTimerPicker = true
                    }

                },
                onDissmis = { showDatePicker = false }
            )
        }
    }
    if (!showDatePicker && showTimerPicker && dateChosed != 0L) {

        AnimatedVisibility(!showDatePicker && showTimerPicker) {
            StickNoteTimePicker(
                onSucces = { hour, minute ->
                   val localDate = Instant.fromEpochMilliseconds(dateChosed)
                       .plus(hour.hours).plus(minute.minutes)
                        .toLocalDateTime(TimeZone.UTC)

                  dataResult  = localDate.date.format(LocalDate.Format {
                      byUnicodePattern("dd/MM/yyyy")
                  })

                    Log.i("INFO", " data resulkt  ${localDate.dayOfMonth}/${localDate.monthNumber}/${localDate.year} $hour:$minute ")
                    Log.i("INFO", " data LocalDate  $localDate")

                    dataResult += " às $hour:$minute"
                   dateChosed = localDate.toInstant(TimeZone.UTC).toEpochMilliseconds()

                    showTimerPicker = false

                     onSelectedDate(dateChosed)
                },
                onCancel = {
                    showTimerPicker = false
                }
            )
        }
    }
    if (isError.first) {
        Text(
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            text = isError.second!!
        )
    }

}


@Preview
@Composable
private fun StickyNoteCalendarPrev() {
    LembretesTheme {
        StickyNoteCalendar(
            date = "12/04/2010",
            onSelectedDate = {},
            isError = false to ""
        )
    }
}
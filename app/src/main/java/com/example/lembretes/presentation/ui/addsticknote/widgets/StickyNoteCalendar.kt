package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.core.Constants
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun StickyNoteCalendar(
    isError: Pair<Boolean, String?>,
    modifier: Modifier = Modifier,
    onSelectedDate: (Long?) -> Unit,
    date: String
) {

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimerPicker by remember { mutableStateOf(false) }
    var dataResult by remember { mutableStateOf(date)}
    var dateChosed by remember { mutableStateOf<LocalDate?>(null) }

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        border = if (isError.first) BorderStroke(1.dp, color = Color.Red)
        else BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
        onClick = {
            showDatePicker = true
        }) {
        Text(
            text = dataResult,
            color = if (!isError.first) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.error,
        )
    }

    if (showDatePicker) {
        AnimatedVisibility(showDatePicker) {
            CalendarWidget(
                modifier = modifier,
                onClick = { date ->
                    showDatePicker = false

                    if (date != null) {
                        dateChosed = Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.UTC).date
                        showTimerPicker = true
                    }

                },
                onDissmis = { showDatePicker = false }
            )
        }
    }
    if (!showDatePicker && showTimerPicker && dateChosed != null) {

        AnimatedVisibility(!showDatePicker && showTimerPicker) {
            StickNoteTimePicker(
                onSucces = { hour, minute ->
                    val localDateTime =
                        LocalDateTime(dateChosed!!, LocalTime(hour = hour, minute = minute))

                    dataResult = localDateTime.format(LocalDateTime.Format {
                        byUnicodePattern("dd/MM/yyyy 'às' HH:mm")
                    })
                    val finalDate =
                        localDateTime.toInstant(Constants.STICK_NOTE_TIME_ZONE)
                            .toEpochMilliseconds()
                    showTimerPicker = false
                    onSelectedDate(finalDate)
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
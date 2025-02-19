package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun StickyNoteCalendar(
    modifier: Modifier = Modifier,
    onSelectedDate: (Long?) -> Unit,
    date: String?
) {


    var showDatePicker by remember { mutableStateOf(false) }
    var dataResult by remember { mutableStateOf(date ?: "Escolha uma Data") }

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            showDatePicker = true
        }) {
        Text(
            text = dataResult,
            color = MaterialTheme.colorScheme.primary,
        )
    }

    if (showDatePicker) {

        AnimatedVisibility(showDatePicker) {
            CalendarWidget(
                modifier = modifier,
                onClick = { date ->
                    showDatePicker = false
                    dataResult  = date?.let {
                        kotlinx.datetime.Instant.fromEpochMilliseconds(it)
                            .toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
                            .date.format(LocalDate.Format {
                                byUnicodePattern("dd/MM/yyyy")
                            })
                    }?: "Escolha uma Data"

                    onSelectedDate(date)
                },
                onDissmis = { showDatePicker = false }
            )
        }
    }
}

@Preview
@Composable
private fun StickyNoteCalendarPrev() {
    LembretesTheme {
        StickyNoteCalendar(
            date = "12/04/2010",
            onSelectedDate = {}
        )
    }
}
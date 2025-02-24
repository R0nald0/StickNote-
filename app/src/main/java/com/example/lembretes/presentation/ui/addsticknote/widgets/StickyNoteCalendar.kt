package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
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
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun StickyNoteCalendar(
    isError : Pair<Boolean,String?>,
    modifier: Modifier = Modifier,
    onSelectedDate: (Long?) -> Unit,
    date: String?
) {

    var showDatePicker by remember { mutableStateOf(false) }
    var dataResult by remember { mutableStateOf(date ?: "Escolha uma Data") }

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        border =  if (isError.first) BorderStroke(1.dp, color = Color.Red)
                  else BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary) ,
        onClick = {
            showDatePicker = true
        }) {
        Text(
            text = dataResult,
            color = if (!isError.first)  MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error,
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
    if (isError.first){
        Text(
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
            ,
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
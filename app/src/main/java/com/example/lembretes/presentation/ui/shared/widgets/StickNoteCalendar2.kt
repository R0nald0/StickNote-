package com.example.lembretes.presentation.ui.shared.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StickNoteCalendar2(
    modifier: Modifier = Modifier,
    selectedDate :LocalDate ,
    onSelectDate:(LocalDate)->Unit
) {
  // Locale.setDefault(Locale("pt","BR"))

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library


    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    HorizontalCalendar(
        modifier = modifier
            .padding(8.dp)
            .background(color = Color.White , shape = RoundedCornerShape(20.dp)),
        state = state,
        dayContent = { Day(
            modifier = modifier,
            day = it,
            isDateSelected = selectedDate,
            onClick = {calendarDay ->  onSelectDate(calendarDay.date)}
        )  },
        monthHeader = { month ->
            val y =month.yearMonth.year
            val m = month.yearMonth.month.getDisplayName(TextStyle.FULL,Locale.getDefault())
            val monthNumber  = month.yearMonth.month.value
            val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
                DaysOfWeekTitle(
                    daysOfWeek = daysOfWeek ,
                    month = m , yearNumber = y, numberMonth = monthNumber)
        }

    )

}

@Composable
fun DaysOfWeekTitle(
    daysOfWeek: List<DayOfWeek>,month:String ,yearNumber :Int ,numberMonth : Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "$numberMonth",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                ),
                )
            Spacer(modifier = Modifier.width(10.dp))
            VerticalDivider(Modifier.height(50.dp) , color = Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = " $month\n  $yearNumber",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 25.sp
                ),

            )

        }
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(modifier: Modifier ,day: CalendarDay,isDateSelected : LocalDate , onClick :(CalendarDay)->Unit ) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .clip(CircleShape)
            .background(
                color = if (isDateSelected == day.date ) {
                    Color.Red
                } else if (day.date == LocalDate.now()) {
                    Color.Green
                } else {
                    Color.White
                }
            )
            .clickable(
                enabled = day.date >= LocalDate.now(),
                onClick = { onClick(day) })
            ,// This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
         // verificando clock na data

        Text(text = day.date.dayOfMonth.toString(),
            color =
            if (day.date <= LocalDate.now().minusDays(1))
                Color.Gray
            else Color.Black
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun StickNoteCalendarPreview() {
    LembretesTheme {
        StickNoteCalendar2(onSelectDate = {}, selectedDate = LocalDate.now())
    }
}
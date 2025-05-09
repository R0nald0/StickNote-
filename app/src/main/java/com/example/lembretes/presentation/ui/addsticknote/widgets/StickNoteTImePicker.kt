package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.R
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickNoteTimePicker(
    modifier: Modifier = Modifier,
    onSucces: (Int,Int) -> Unit,
    onCancel: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = Clock.System.now().toLocalDateTime(TimeZone.UTC).hour + 2,
        initialMinute = Clock.System.now().toLocalDateTime(TimeZone.UTC).hour,
    )
    BasicAlertDialog(
        modifier = modifier.padding(8.dp),
        onDismissRequest = onCancel,
    ) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(22.dp)
            ) {
                TimePicker(
                    colors = TimePickerDefaults.colors(
                        selectorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        clockDialColor = MaterialTheme.colorScheme.primary,
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.primary,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    state  = timePickerState
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right
                ) {
                    TextButton(
                        onClick = onCancel
                    ) {
                        Text(
                            stringResource(R.string.cancelar),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                            )
                        )
                    }
                    TextButton(
                        onClick = {
                            val hour = timePickerState.hour
                            val minute = timePickerState.minute
                            onSucces(hour,minute)
                            onCancel()
                        }
                    ) {
                        Text(
                            stringResource(R.string.aceitar),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun StickNoteTimePickerPrev() {
    LembretesTheme {
        StickNoteTimePicker(
            onCancel = {},
            onSucces = {h,m ->}
        )
    }
}
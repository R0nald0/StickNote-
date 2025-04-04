package com.example.lembretes.presentation.ui.shared.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lembretes.core.Constants.STICK_NOTE_TIME_ZONE
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.utils.dateFormatToString
import com.example.lembretes.utils.getDateFronLongOfCurrentSystemDate
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

@Composable
fun StickNoteCardView(
    stickyNoteDomain: StickyNoteDomain,
    onUpdateStateNotificaion: (StickyNoteDomain?) -> Unit,
    modifier: Modifier
) {
    val dateIsNotOnPass by remember {

        val actualDate = Clock.System.now().toLocalDateTime(STICK_NOTE_TIME_ZONE)
        val dateChosedByUser = Clock.System.getDateFronLongOfCurrentSystemDate(stickyNoteDomain.dateTime)

        Log.i("INFO_", "actualDate: $actualDate")
        Log.i("INFO_", "dateChosedByUser: $dateChosedByUser")

        mutableStateOf(
            actualDate < dateChosedByUser
        )
    }
    Card(
        onClick = {
            //context.showNotification(stickyNoteDomain.name,stickyNoteDomain.description)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.padding(4.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stickyNoteDomain.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                },
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                ),
            )
            IconButton(
                modifier = modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .size(35.dp),
                onClick = {
                    if (!dateIsNotOnPass) {
                        onUpdateStateNotificaion(null)
                        return@IconButton
                    }
                    stickyNoteDomain.isRemember = !stickyNoteDomain.isRemember
                    onUpdateStateNotificaion(stickyNoteDomain)

                }, content = {
                    if (dateIsNotOnPass) {
                        Icon(
                            Icons.Filled.Notifications, contentDescription = "notification icon",
                            tint = if (stickyNoteDomain.isRemember) MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.outline
                        )
                    } else {
                        Icon(
                            Icons.Filled.Check, contentDescription = "notification icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                })
        }
        Text(
            text = stickyNoteDomain.description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            },
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            maxLines = 2,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {

            StickChips(
                label = stickyNoteDomain.dateTime.dateFormatToString(),
                isSelected = true,
                colorBackGround = MaterialTheme.colorScheme.onPrimaryContainer,
                colorText = MaterialTheme.colorScheme.primary
            )

            stickyNoteDomain.tags.forEach { label ->
                StickChips(
                    label = label,
                    isSelected = true,
                    colorBackGround = MaterialTheme.colorScheme.onPrimaryContainer,
                    colorText = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }

}

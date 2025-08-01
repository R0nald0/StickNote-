package com.example.lembretes.presentation.ui.shared.widgets

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.theme.GlobalSizeFont
import com.example.lembretes.utils.dateFormatToString
import com.example.lembretes.utils.getDateCurrentSystemDefaultInLocalDateTime
import com.example.lembretes.utils.getDateFromLongOfCurrentSystemDate
import kotlinx.datetime.Clock
import java.util.Locale

@Composable
fun StickNoteCardView(
    stickyNoteDomain: StickyNoteDomain,
    onUpdateStateNotification: (Boolean) -> Unit,
    modifier: Modifier
) {
    val snackBarHots by remember { mutableStateOf(SnackbarHostState())  }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dateIsNotOnPass by remember {
        val actualDate = Clock.System.getDateCurrentSystemDefaultInLocalDateTime()
        val dateChosedByUser = Clock.System.getDateFromLongOfCurrentSystemDate(stickyNoteDomain.dateTime)
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,

                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = GlobalSizeFont.titleSize.sp,
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
                        onUpdateStateNotification(stickyNoteDomain.isRemember)
                        return@IconButton
                    }

                    onUpdateStateNotification(!stickyNoteDomain.isRemember)

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
                fontSize = GlobalSizeFont.descriptionSize.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            maxLines = 2,
            modifier = Modifier
                .padding(start = 15.dp, bottom = 8.dp)
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

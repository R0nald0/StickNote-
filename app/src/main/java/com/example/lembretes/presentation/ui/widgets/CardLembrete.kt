package com.example.lembretes.presentation.ui.widgets

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.core.notification.showNotification
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.ui.theme.Purple40
import com.example.lembretes.presentation.ui.theme.Purple80
import com.example.lembretes.utils.convertDateLongToString
import com.example.lembretes.utils.convertDateStringToLong
import java.util.Date
import java.util.Locale

@Composable
fun StickNoteCardView(
    stickyNoteDomain: StickyNoteDomain,
    onUpdateStateNotificaion : (Int,Boolean)->Unit,
    context: Context,
    modifier: Modifier
) {

    Card(
        onClick = {
            context.showNotification(stickyNoteDomain.name,stickyNoteDomain.description)
        },
        colors = CardDefaults.cardColors(
            containerColor = Purple40
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = modifier.padding(4.dp)
    ) {

        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stickyNoteDomain.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()) else it.toString() },
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White
                ),
            )
            IconButton(
                modifier = modifier
                    .clip(CircleShape)
                    .background(Purple80)
                    .size(35.dp),
                onClick = {
                        stickyNoteDomain.isRemember = !stickyNoteDomain.isRemember
                        onUpdateStateNotificaion(stickyNoteDomain.id!!,stickyNoteDomain.isRemember)

                }, content = {
                    Icon(
                        Icons.Filled.Notifications
                        ,contentDescription = "notification icon",
                        tint =  if(stickyNoteDomain.isRemember) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.tertiary
                    )
                })
        }
        Text(
            text = stickyNoteDomain.description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()) else it.toString()},
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = Color.Gray,
            ),
            maxLines = 2,
            modifier = Modifier
                .padding(start =  12.dp, bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()

        ) {
            StickChips(
                label =  Date().convertDateLongToString(stickyNoteDomain.dateTime?: Date().time)?:"00/00/0000",
                isSelected = true,
                colorBackGround = Color.White,

                )
            StickChips(
                label = "#urgegnte",
                isSelected = true,
                colorBackGround = Color.White,
                colorText = Color.Red,

                )
            StickChips(
                label =  "#Med",
                isSelected = true,
                colorBackGround = Color.White,
                colorText = Color.Red,
            )
        }
    }

}

@Composable
fun MyCardView(modifier: Modifier.Companion, stickyNoteDomain: StickyNoteDomain, onUpdateStateNotificaion: Any, context: Context) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun MyCardViewPreview () {
    val context = LocalContext.current
    LembretesTheme {
        MyCardView(
            modifier = Modifier,
            stickyNoteDomain = StickyNoteDomain(
                id = 1,
                name = "Correr",
                description = "Correr pela manhã dasdasdasdsadadasdsadasdsadasdasdsaadasdadsadsadsadadadsadsadasdasdsada",
                dateTime = Date().convertDateStringToLong("10/10/2023")!!,
                isRemember = false),
                onUpdateStateNotificaion = { indx :Int, remember :Boolean->},
                context = context
        )
    }
}
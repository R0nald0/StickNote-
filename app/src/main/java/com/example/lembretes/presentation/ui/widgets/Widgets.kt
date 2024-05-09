package com.example.lembretes.presentation.ui.widgets

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.CalendarContract.Colors
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.R
import com.example.lembretes.core.notification.showNotification
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.dateForExtense
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.ui.theme.Purple40
import com.example.lembretes.presentation.ui.theme.Purple80
import com.example.lembretes.utils.convertDateLongToString
import java.util.Date
import java.util.Locale
import java.util.logging.Filter


@Composable
fun StickChips(
    modifier: Modifier = Modifier,
    label:String,
    isSelected :Boolean =false,
    colorBackGround : Color = Color.Gray,
    colorText:Color = Color.Black
) {
    Text(
        label,
        color = colorText,
        modifier = if (isSelected) modifier
            .drawBehind {
                drawRoundRect(color = colorBackGround , cornerRadius = CornerRadius(10.dp.toPx()))
            }
            .padding(8.dp)

        else modifier
            .background(color = Color.Transparent) ,
        style = if (isSelected)  MaterialTheme.typography.bodyMedium.copy(
            fontSize = 12.sp, fontWeight = FontWeight.Bold
        )
           else MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,color = Color.Gray)
    )

}


@Composable
fun MyCardView(
    stickyNoteDomain: StickyNoteDomain,
    onUpdateStateNotificaion : (StickyNoteDomain)->Unit,context: Context,
    modifier: Modifier
) {

    var rememberStickNote by rememberSaveable {
        mutableStateOf(stickyNoteDomain.isRemember)
    }

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
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)
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
                        modifier = modifier.clip(CircleShape).background(Purple80).size(35.dp),
                        onClick = {
                        rememberStickNote = !rememberStickNote
                        val stickUpdate = StickyNoteDomain(
                            id = stickyNoteDomain.id,
                            dateTime = stickyNoteDomain.dateTime,
                            isRemember = rememberStickNote,
                            description = stickyNoteDomain.description,
                            name = stickyNoteDomain.name
                        )
                        onUpdateStateNotificaion(stickUpdate)
                   }, content = {
                            Icon(
                                Icons.Filled.Notifications
                                ,contentDescription = "notification icon",
                                tint =  if(rememberStickNote) MaterialTheme.colorScheme.onPrimary
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
                    modifier = Modifier.padding( 10.dp)
                        .fillMaxWidth()

                ) {
                     StickChips(
                         label =  Date().convertDateLongToString(stickyNoteDomain.dateTime)?:"00/00/0000",
                          isSelected = true,
                         colorBackGround = Color.White
                         )
                    StickChips(
                        label = "#urgegnte",
                        isSelected = true,
                        colorBackGround = Color.White,
                        colorText = Color.Red
                    )
                    StickChips(
                        label =  "#Med",
                        isSelected = true,
                        colorBackGround = Color.White,
                        colorText = Color.Red
                    )
                    }
                }

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
            dateTime =  Date().time,
            isRemember = false),
            onUpdateStateNotificaion = {}, context = context
        )
    }
}


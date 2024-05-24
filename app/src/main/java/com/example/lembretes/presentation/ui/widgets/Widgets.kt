package com.example.lembretes.presentation.ui.widgets

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.core.notification.showNotification
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.ui.theme.Purple40
import com.example.lembretes.presentation.ui.theme.Purple80
import com.example.lembretes.utils.convertDateLongToString
import com.example.lembretes.utils.convertDateStringToLong
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun StickChips(
    modifier: Modifier = Modifier,
    label:String,
    isSelected :Boolean =false,
    colorBackGround : Color = Color.LightGray,
    colorText:Color = Color.Black ,
    onClick : ()->Unit ={ }
) {


    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSelected) 10.dp else 0.dp
        ),
        onClick =onClick) {
        Text(
            text = label,
            color = colorText,
            modifier = if (isSelected) modifier
                .drawBehind {
                    drawRoundRect(
                        color = colorBackGround,
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                }
                .padding(8.dp)

            else modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(6.dp),
                style = if (isSelected)  MaterialTheme.typography.bodyMedium
                    .copy(
                       fontSize = 12.sp,
                       fontWeight = FontWeight.Bold
            )
            else MaterialTheme.typography.bodySmall
                .copy(
                    color = Color.Gray,
                )
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SticnkNoteToolBar(
    modifier: Modifier = Modifier,
    scroolBehavior: TopAppBarScrollBehavior,
    title : String,
    isColapsed :Boolean = false,
    numberOfStickNotes :Int = 0
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        scrollBehavior = scroolBehavior,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon( Icons.Default.AccountCircle, contentDescription ="User avatar" )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon( Icons.Default.Menu, contentDescription ="User menu" )
            }
        },

        title = {
             if (isColapsed){
                 Column(
                 ) {
                     Text(
                         text = title,

                     )

                     Row(
                         horizontalArrangement = Arrangement.Center,
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         val agendadosText = if (numberOfStickNotes <= 1)  "lembrete agendado" else "lembretes agendados"
                         Text(
                             text = "Você tem " ,
                             fontSize = 13.sp,
                         )
                         Text(
                             text =  "$numberOfStickNotes ",
                             style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                         )
                         Text(
                             text = agendadosText ,
                             fontSize = 13.sp,
                         )
                     }
                 }
             }else{
                 Text(
                     text = title,
                     style = MaterialTheme.typography.titleLarge
                     )
             }
        }
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
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()

                ) {
                     StickChips(
                         label =  Date().convertDateLongToString(stickyNoteDomain.dateTime?:Date().time)?:"00/00/0000",
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
            onUpdateStateNotificaion = {}, context = context
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarWidget(
    datePickerState : DatePickerState,
    modifier: Modifier = Modifier,
    selectableDates: Long,
    onDissmis: () -> Unit,
    onClick: () -> Unit
    ) {

        val selectedDate =selectableDates
        val dateFormat= SimpleDateFormat("dd/MM/yyy", Locale("pt-BR"))

        DatePickerDialog(
            onDismissRequest = onDissmis,
            confirmButton = {
                TextButton(
                    onClick = onClick
                ){
                    Text(text = "ok")
                }

            }) {
            DatePicker(state = datePickerState)

    }
}


package com.example.lembretes.presentation.ui.shared.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.presentation.ui.theme.LembretesTheme


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
        onClick  = onClick
    ) {
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
                       fontWeight = FontWeight.W900
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
    scroolBehavior: TopAppBarScrollBehavior ,
    title : String,
    isColapsed :Boolean = false,
    numberOfStickNotes :Int = 0,
    isOpenDrawer : ()->Unit,
    onOpenProfile : ()->Unit,
    openSearch : ()->Unit,
) {
    LargeTopAppBar(
        modifier = modifier.clip(
            RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)
        ),
        colors = TopAppBarDefaults.topAppBarColors(
                containerColor =  MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary

        ),
        scrollBehavior = scroolBehavior,
        actions = {
            IconButton(onClick = openSearch) {
                Icon( Icons.Default.Search, contentDescription ="User avatar" )
            }
            IconButton(onClick = onOpenProfile) {
                Icon( Icons.Default.AccountCircle, contentDescription ="User avatar" )
            }

        },
        navigationIcon = {
            IconButton(onClick = isOpenDrawer) {
                Icon( Icons.Default.Menu, contentDescription ="User menu" )
            }
        },

        title = {
             if (isColapsed){
                 Column(
                 ) {
                     Text(
                         text = title,
                         style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                     )

                     Row(
                         horizontalArrangement = Arrangement.Center,
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         val agendadosText = if (numberOfStickNotes <= 1)  "lembrete agendado" else "lembretes agendados"
                         Text(
                             text = "Você tem" ,
                             fontSize = 13.sp,
                         )
                         Text(
                             text =  " $numberOfStickNotes ",
                             style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                         )
                         Text(
                             text = agendadosText ,
                             fontSize = 13.sp,
                         )
                     }
                 }
             }else{
                 Text(text = title, style = MaterialTheme.typography.titleLarge)
             }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SticnkNoteToolBarPreview() {
    LembretesTheme {
        SticnkNoteToolBar(
            isOpenDrawer = { /*TODO*/ },
            onOpenProfile = { /*TODO*/ },
            scroolBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            )  ,
            title ="Stick Note",
            openSearch = {}
        )
    }
}





package com.example.lembretes.presentation.ui.addsticknote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickyNoteCalendar
import com.example.lembretes.presentation.ui.shared.widgets.LoadingScreen
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteAppBar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteTextField
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.AddUpdateViewModel
import com.example.lembretes.utils.convertDateLongToString
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddStickNoteScreen (
    modifier: Modifier = Modifier,
    idStikcNote :String? = null,
    onClosed: () -> Unit,
    ) {

    val addUpdateViewModel = hiltViewModel<AddUpdateViewModel>()

    var loader by remember {
        mutableStateOf(true)
    }

    Log.i("INFO_", "AddStickNoteScreen: loader : $loader")

    if (loader){
        LoadingScreen(isLoading = {
             if (idStikcNote != "0"){
                 addUpdateViewModel.findById(idStikcNote!!.toInt())
             }
         loader = false
        })
    }else{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val ui by addUpdateViewModel.addScreenUi.collectAsStateWithLifecycle()

            if (idStikcNote != null && idStikcNote !="0"){
                if (ui.stickyNoteDomain.name.isNotBlank()){
                    MyScreen(
                        onSave = addUpdateViewModel::updateStickNote,
                        stickyNoteDomain =  ui.stickyNoteDomain,
                        onClosed =   onClosed,
                    )
                }

            }else{
                MyScreen(
                    onSave = addUpdateViewModel::insertStickNote,
                    stickyNoteDomain =null,
                    modifier = modifier,
                    onClosed = onClosed
                )
            }
        }

    }

  if (idStikcNote != null && idStikcNote !="0"){
        Log.d("INFO_", "AddStickNoteScreen: $idStikcNote")
    }else{
        Log.d("INFO_", "AddStickNoteScreen: $idStikcNote")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyScreen(
    onSave :(StickyNoteDomain) -> Unit ,
    stickyNoteDomain: StickyNoteDomain?,
    modifier: Modifier =Modifier,
    onClosed : ()->Unit
)
{
    Scaffold (
        topBar = { StickNoteAppBar (
            onClosed = onClosed,
            title = if (stickyNoteDomain != null) stringResource(R.string.editar_lembrete) else stringResource(
                R.string.adicionar_lembrete
             )
            )
        }
    ){paddingValues->
        var lembreteName by rememberSaveable {
            mutableStateOf(  stickyNoteDomain?.name ?: "")
        }
        var lembreteDescription by rememberSaveable {
            mutableStateOf(  stickyNoteDomain?.description?:"")
        }
        var isRemeber by rememberSaveable {
            mutableStateOf( stickyNoteDomain?.isRemember ?:false)
        }

        val tags = remember {
            mutableStateListOf<String>()
        }

        var tag by remember { mutableStateOf("") }
        val datePickerState = rememberDatePickerState(
            selectableDates = object  : SelectableDates{
                val zoneId =  TimeZone.getTimeZone("UTC").toZoneId()
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val selectableDates =Instant.ofEpochMilli(utcTimeMillis).atZone(zoneId)
                    val currentData = ZonedDateTime.now(zoneId)
                    val fiveDaysRange = currentData.plusDays(4)
                    return super.isSelectableDate(utcTimeMillis)
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return true
                 }
            }
        )

        var selectedDate by remember {
            mutableStateOf(LocalDate.now())
        }

     val date  = stickyNoteDomain?.let{
            Date().convertDateLongToString(it.dateTime)
     }


        var dataResult: String? by remember {
            mutableStateOf(date)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                ,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = modifier.size(120.dp),
                painter = painterResource(id = R.drawable.iconstickynote),
                contentDescription ="Stick note icon" )

            Spacer(modifier = modifier.height(5.dp))
            StickNoteTextField(
                maxLines = 1,
                value = lembreteName ,
                label ="Título" ,
                isError = false,
                onChange = {value-> lembreteName = value},
                singleLine = true ,
                icon = { /*TODO*/ },
                trailingIcon = { /*TODO*/ },
                supportTexting = {}
            )

            StickNoteTextField(
                maxLines = 5,
                value =  lembreteDescription,
                label ="Descrição" ,
                isError = false,
                onChange = {value->
                    lembreteDescription = value
                },
                singleLine = false,
                icon = { /*TODO*/ },
                trailingIcon = { /*TODO*/ },
                supportTexting = {}
            )

             Spacer(modifier.height(15.dp))

             LaunchedEffect(key1 = stickyNoteDomain?.tags?.isNotEmpty()) {
                 if (stickyNoteDomain != null && stickyNoteDomain.tags.isNotEmpty()){
                     tags.addAll(stickyNoteDomain.tags)
                 }
             }
             StickNoteTagArea(
                 modifier = modifier,
                 limitCha = 3,
                 label = "Tag",
                 tags =  tags,
                 tag = tag,
                 onAdd = {
                         if (tag.isBlank()) return@StickNoteTagArea
                         if (tags.size <= 1){
                             val tagFormat = tag.replaceBefore(tag.first(),"#")
                             tags.add(tagFormat)
                             tag =""
                         }
                     },
                 onRemove = {
                     if (tags.size > 0){
                         Log.i("INFO_", "MyScreen: $it")
                         tags.remove(it)
                     }
                 },
                 onTextChange = {
                     if (it.length <= 3){
                         tag = it.lowercase()
                     }
                 }
             )

             Spacer(modifier.height(20.dp))
        StickyNoteCalendar(
              modifier =  modifier,
                datePickerState = datePickerState,
                dateResult =  dataResult ?:"Escolher data",
                onSelectedDate = { date->
                    dataResult = date.toString()
                   selectedDate = date?.toLocalDate()
               }
            )

            Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .clickable {
                       isRemeber = !isRemeber
                   },
               horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
           ) {
               Checkbox(checked =  isRemeber,
                   onCheckedChange ={isChecek ->
                   isRemeber = isChecek
               } )
               Text(text = "Relembrar?")
           }

           Row(
               modifier = modifier
                   .fillMaxWidth()
                   .padding(20.dp),
              horizontalArrangement = Arrangement.End
           ) {
               ElevatedButton(
                   enabled =  validateField(lembreteName,lembreteDescription),
                   onClick = {
                       val stickNote = StickyNoteDomain(
                           id = stickyNoteDomain?.id,
                           name = lembreteName,
                           description = lembreteDescription,
                           dateTime = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                           isRemember = isRemeber,
                           tags = tags
                       )

                     onSave(stickNote)
                       onClosed()
                   }) {
                   Text("Salvar")
               }
           }
        }
    }
}





@Composable
fun StickNoteTagArea(
    modifier: Modifier = Modifier,
    label : String,
    limitCha : Int ,
    tags : List<String>,
    tag :String,
    onAdd : ()->Unit,
    onRemove :(String)->Unit,
    onTextChange:(String)->Unit
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StickNoteTextField(
                modifier = Modifier.fillMaxWidth(0.6f),
                value = tag,
                label = label,
                isError = false,
                enable = tags.size < 2,
                maxLines = 1,
                onChange = onTextChange ,
                supportTexting = {
                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = "${tag.length}/$limitCha")
                    }
                },
                singleLine =true ,
                icon = { /*TODO*/ },
                trailingIcon = { /*TODO*/ }
            )
            IconButton(
                modifier = Modifier.
                background(color = Color.Blue , shape = CircleShape),
                onClick = onAdd) {
                Icon(Icons.Filled.Add, contentDescription ="" , tint = Color.Green)
            }
        }

       Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
        ) {
            tags.forEach {
                InputChip(
                    selected = true,
                    trailingIcon = { Icon( Icons.Filled.Close, contentDescription ="" )},
                    onClick = {onRemove(it)} ,
                    label = {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                             ),
                            )
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview
@Composable
private fun StickNoteSurFaceHasTagAreaPrev() {
    LembretesTheme {
        StickNoteTagArea(
            limitCha = 3,
            label = "Tag",
            tags = listOf("#tes","#tag"),
            tag = "",
            onTextChange = {},
            onRemove = {},
            onAdd = {}
        )
    }
}

fun validateField(name: String , desciption : String):Boolean{
    if (name.isNotBlank() && desciption.isNotBlank()){
        return true
    }
    return false
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MyScreenPreview() {
    LembretesTheme {
        MyScreen({},null, onClosed = {})
    }
}



package com.example.lembretes.presentation.ui.addsticknote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.ui.widgets.CalendarWidget
import com.example.lembretes.presentation.viewmodel.AddUpdateViewModel
import com.example.lembretes.utils.DataUtil
import com.example.lembretes.utils.convertDateLongToString
import com.example.lembretes.utils.convertDateStringToLong
import com.example.lembretes.utils.dateTomorow
import java.util.Date


@Composable
fun AddStickNoteScreen (
    modifier: Modifier = Modifier,
    stickyNoteDomain: StickyNoteDomain? = null,
    stickNoteViewmodel :AddUpdateViewModel,
    onClosed: () -> Unit,
    reloadoList :()->Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        if (stickyNoteDomain != null){
            MyScreen(
                onSave = stickNoteViewmodel::updateStickNote,
                stickyNoteDomain =  stickyNoteDomain,
                onClosed =   onClosed,
                reloadoList = reloadoList
                )
        }else{
            MyScreen(
                onSave = stickNoteViewmodel::insertStickNote,
                stickyNoteDomain =null,
                modifier = modifier,
                onClosed =   onClosed,
                reloadoList = reloadoList
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(
    onSave :(StickyNoteDomain) -> Unit ,
    stickyNoteDomain: StickyNoteDomain?,
    modifier: Modifier =Modifier,
    onClosed : ()->Unit,
    reloadoList :()->Unit
)
{
    Scaffold (
        topBar = { StickNoteAppBar (onClosed = onClosed, stickyNoteDomain = stickyNoteDomain)}
    ){paddingValues->
        var lembreteName by rememberSaveable { mutableStateOf(stickyNoteDomain?.name ?: "") }
        var lembreteDescription by rememberSaveable { mutableStateOf(stickyNoteDomain?.description ?: "") }
        var isRemeber by rememberSaveable { mutableStateOf(stickyNoteDomain?.isRemember ?: false) }

        var dateResult by rememberSaveable {mutableStateOf(
            if(stickyNoteDomain != null)
                Date().convertDateLongToString( stickyNoteDomain.dateTime) ?: "Escolha uma data"
            else "Escolha uma data"

        )}

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
            OutlinedTextField(
                maxLines = 1,
                value = lembreteName,
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text(text = "Nome do Lembrete")
                }, onValueChange ={
                    lembreteName = it
                } )

            OutlinedTextField(
                maxLines = 10,
                value = lembreteDescription,
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text(text = "Descricão")
                }, onValueChange ={
                    lembreteDescription = it
                } )
             Spacer(modifier.height(20.dp))

             StickyNoteCalendar(
                 modifier = modifier,
                 dateResult = dateResult,
                 onSelectedDate = {
                     dateResult = it
                 }
             )

           Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically,
           ) {
               Checkbox(checked = isRemeber, onCheckedChange ={isChecek ->
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
                   enabled = validateField(lembreteName,lembreteDescription),
                   onClick = {
                       val stickNote = StickyNoteDomain(
                           id = stickyNoteDomain?.id,
                           name = lembreteName,
                           description = lembreteDescription,
                           dateTime = Date().convertDateStringToLong(dateResult) ?: Date().time,
                           isRemember = isRemeber
                       )
                       onSave(stickNote)
                       reloadoList()
                       onClosed()
                   }) {
                   Text("Salvar")
               }
           }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StickNoteAppBar(
      modifier: Modifier = Modifier,
      onClosed:()->Unit,
      stickyNoteDomain: StickyNoteDomain?
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onClosed,
                content = {
                    Icon(Icons.Default.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(
                text = if (stickyNoteDomain != null) stringResource(R.string.editar_lembrete) else stringResource(
                    R.string.adicionar_lembrete
                ),
                color = MaterialTheme.colorScheme.primary
            )
        },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickyNoteCalendar(
    modifier: Modifier = Modifier,
    onSelectedDate :(String)->Unit,
    dateResult : String
    ) {

    val currentYear =DataUtil().getCurrentyear()
    val datePickerState  = rememberDatePickerState(

        yearRange = IntRange(currentYear, currentYear +3),
    )
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            showDatePicker = true
        }) {
        Text(text = dateResult )
    }

    if(showDatePicker){
        CalendarWidget(
            datePickerState =datePickerState,
            selectableDates = datePickerState.selectedDateMillis ?: Date().time,
            onClick = {
                showDatePicker = false
                val selcc  = Date().dateTomorow(datePickerState.selectedDateMillis ?: Date().time)

                onSelectedDate(selcc)
            },
            onDissmis = {showDatePicker = false}
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
        MyScreen({},null, onClosed = {}, reloadoList = {})
    }
}



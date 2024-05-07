package com.example.lembretes.presentation

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.utils.convertDateLongToString
import com.example.lembretes.utils.convertDateStringToLong
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AddStickNoteActivity : ComponentActivity() {
    private val stickNoteViewmodel by viewModels<StickNoteViewmodel>()
     var stickyNoteDomain: StickyNoteDomain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LembretesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val bundle = intent.extras
                    if (bundle != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val stick  = bundle.getSerializable("stick",StickyNoteDomain::class.java)
                            stick.let { stickyNoteBundle ->
                                stickyNoteDomain = stickyNoteBundle
                            }
                        }else{
                            val stick  = bundle.getSerializable("stick") as StickyNoteDomain
                            stick.let { stickyNoteBundle ->
                                stickyNoteDomain = stickyNoteBundle

                            }
                        }
                    }
                  MyScreen(onSave = if (stickyNoteDomain != null )
                                         stickNoteViewmodel::updateStiickNote
                                    else stickNoteViewmodel::insertStickNote,
                               stickyNoteDomain)
                }
            }
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(onSave :(StickyNoteDomain) -> Unit ,stickyNoteDomain: StickyNoteDomain?) {
    val activity = (LocalContext.current as? Activity)
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            activity?.finish()
                        },
                        content = {
                          Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "", tint = MaterialTheme.colorScheme.inversePrimary)
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
    ){paddingValues->
        var lembreteName by rememberSaveable { mutableStateOf(stickyNoteDomain?.name ?: "") }
        var lembreteDescription by rememberSaveable { mutableStateOf(stickyNoteDomain?.description ?: "") }
        val datePickerState  = rememberDatePickerState(

        )
        var showDatePicker by remember { mutableStateOf(false) }

        var dateResult by rememberSaveable {mutableStateOf(
             if(stickyNoteDomain != null)
                 Date().convertDateLongToString(stickyNoteDomain.dateTime) ?: "Escolha uma data"
                 else "Escolha uma data"

        )}
        var isRemeber by rememberSaveable { mutableStateOf(stickyNoteDomain?.isRemember ?: false) }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                maxLines = 1,
                isError = if (lembreteName.isBlank()) true else false,
                value = lembreteName,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Nome do Lembrete")
                }, onValueChange ={
                    lembreteName = it
                } )

            OutlinedTextField(
                maxLines = 10,
                isError = if (lembreteDescription.isBlank()) true else false,
                value = lembreteDescription,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Descricão")
                }, onValueChange ={
                    lembreteDescription = it
                } )
            Spacer(Modifier.height(20.dp))

             OutlinedButton(
                 modifier = Modifier.fillMaxWidth(),
                 onClick = {
                     showDatePicker = true
                 }) {
                 Text(text = dateResult)
             }
            if (showDatePicker){
                DatePickerDialog(

                    onDismissRequest = { showDatePicker = false},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ){
                            Text(text = "ok")
                        }

                       dateResult = Date().convertDateLongToString(datePickerState.selectedDateMillis ?: Date().time.plus(10))  ?: "No value selectede"
                    }) {
                    DatePicker(state = datePickerState)

                }
            }
             
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
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(20.dp),
              horizontalArrangement = Arrangement.End
           ) {
               ElevatedButton(

                   onClick = {
                       val stickNote = StickyNoteDomain(
                           id = if (stickyNoteDomain != null) stickyNoteDomain.id else null,
                           name = lembreteName,
                           description = lembreteDescription,
                           dateTime = Date().convertDateStringToLong(dateResult)?:Date().time.plus(10),
                           isRemember = isRemeber
                       )

                       onSave(stickNote)
                       activity?.finish()
                   }) {
                   Text("Salvar")
               }
           }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MyScreenPreview() {
    LembretesTheme {
        MyScreen({},null)
    }
}



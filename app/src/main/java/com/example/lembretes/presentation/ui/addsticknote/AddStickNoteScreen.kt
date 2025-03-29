package com.example.lembretes.presentation.ui.addsticknote

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.core.notification.StickNoteAlarmManeger
import com.example.lembretes.core.widgets.ShowAndCheckShouldRationale
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteCheckBox
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteTagArea
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickyNoteCalendar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteAppBar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteTextField
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.AddUpdateViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddStickNoteScreen(
    modifier: Modifier = Modifier,
    idStikcNote: Int,
    onClosed: () -> Unit,
    activity:  Activity
) {

    val addUpdateViewModel = hiltViewModel<AddUpdateViewModel>()
    /*
        var loader by remember {
            mutableStateOf(true)
        }

        if (loader) {
            LoadingScreen(isLoading = {
                if (idStikcNote != "0") {
                    addUpdateViewModel.findById(idStikcNote!!.toInt())
                }
                loader = false
            })
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                val ui by addUpdateViewModel.addScreenUi.collectAsStateWithLifecycle()

                if (idStikcNote != null && idStikcNote != "0") {
                    if (ui.stickyNoteDomain.name.isNotBlank()) {
                        MyScreen(
                            onSave = addUpdateViewModel::updateStickNote,
                            stickyNoteDomain = ui.stickyNoteDomain,
                            onClosed = onClosed,
                        )
                    }

                } else {
                    MyScreen(
                        onSave = addUpdateViewModel::validateFieldStickNote,
                        stickyNoteDomain = ui.stickyNoteDomain,
                        modifier = modifier,
                        onClosed = onClosed
                    )
                }
            }

        }*/
    //TODO adicionar loading ao clicar em salvar
    //TODO verificar quando so esccolhido data e cancelar data fica em branco
    //TODO verificar quando ao receber os dados para editar

    
    if (idStikcNote != 0) {
        SideEffect {
            addUpdateViewModel.findById(idStikcNote)
        }
    }

   val context = LocalContext.current


    MyScreen(
        onSave = {stickNote ->
            if (idStikcNote != 0) {
                addUpdateViewModel.updateStickNote(stickyNoteDomain = stickNote){
                   onActionAfterCreate(context = context, stickNote = stickNote)
                    onClosed()
                }
            }
            else {
                addUpdateViewModel.insertStickNote(stickyNoteDomain = stickNote){
                   onActionAfterCreate  (context = context, stickNote = stickNote)
                    onClosed()
                }
            }
        },
        viewModel = addUpdateViewModel,
        activity=  activity,
        modifier = modifier,
        onClosed = onClosed,
    )
}


@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun MyScreen(
    onSave: (StickyNoteDomain) -> Unit,
    viewModel: AddUpdateViewModel = viewModel(),
    activity: Activity,
    modifier: Modifier = Modifier,
    onClosed: () -> Unit
) {
    val ui by viewModel.addScreenUi.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            StickNoteAppBar(
                onClosed = onClosed,
                title = if (ui.stickyNoteDomain.id != null) stringResource(R.string.editar_lembrete)
                else stringResource(R.string.adicionar_lembrete)
            )
        }
    ) { paddingValues ->
        var isRemember by rememberSaveable { mutableStateOf(false) }
        val tags = remember { mutableStateListOf<String>() }
        var tag by remember { mutableStateOf("") }
        var showRationale by remember {
            mutableStateOf(false)
        }
        val lauche = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.map {
                if (!it.value){
                    ContextCompat.checkSelfPermission(activity, it.key)
                    Log.i("INFO", "onCreate: ${it.key}:${it.value} ")
                }
            }
        }

        if (showRationale){
            ShowAndCheckShouldRationale(
                modifier = modifier,
                activity =activity ,
                permission = Manifest.permission.POST_NOTIFICATIONS,
                messagem = stringResource(R.string.permission_explication),
                onCancel = {showRationale = false},
                onAccept = {
                    showRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lauche.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                    }
                }
            )
        }

        var selectedDate: Long? by rememberSaveable {
            mutableStateOf(
                ui.stickyNoteDomain.dateTime
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var lembreteName by rememberSaveable { mutableStateOf(ui.stickyNoteDomain.name) }

            var lembreteDescription by rememberSaveable {
                mutableStateOf(ui.stickyNoteDomain.description)
            }

            Image(
                modifier = modifier.size(120.dp),
                painter = painterResource(id = R.drawable.agenda),
                contentDescription = "Logo aplicativo"
            )

            Spacer(modifier = modifier.height(16.dp))

            StickNoteTextField(
                maxLines = 1,
                value = lembreteName,
                label = "Título",
                isError = ui.erros.containsKey("title"),
                onChange = { value ->
                    lembreteName = value
                },
                singleLine = true,
                icon = {},
                trailingIcon = {
                    if (ui.erros.containsKey("title")) {
                        Icon(
                            Icons.Outlined.Info, contentDescription = "Info icon",
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                },
                supportTexting = {
                    Text(ui.erros["title"] ?: "")
                }
            )


            StickNoteTextField(
                maxLines = 5,
                value = lembreteDescription,
                label = stringResource(R.string.descri_o),
                isError = ui.erros.containsKey("description"),
                onChange = { value ->
                    lembreteDescription = value
                },
                singleLine = false,
                icon = { },
                trailingIcon = {},
                supportTexting = {
                    Text(ui.erros["description"] ?: "")
                }
            )

            Spacer(modifier.height(15.dp))

            LaunchedEffect(key1 = ui.stickyNoteDomain.tags.isNotEmpty()) {
                val stickyNoteDomain = ui.stickyNoteDomain
                if (stickyNoteDomain.tags.isNotEmpty()) {
                    tags.addAll(stickyNoteDomain.tags)
                }
            }

            StickNoteTagArea(
                modifier = Modifier,
                limitCha = 3,
                label = "Tag",
                tags = tags,
                tag = tag,
                onAdd = {
                    if (tag.isBlank()) return@StickNoteTagArea
                    if (tags.size <= 1) {
                        val tagFormat = tag.replaceBefore(tag.first(), "#")
                        tags.add(tagFormat)
                        tag = ""
                    }
                },
                onRemove = {
                    if (tags.isNotEmpty()) {
                        tags.remove(it)
                    }
                },
                onTextChange = {
                    if (it.length <= 3) {
                        tag = it.lowercase()
                    }
                }
            )

            Spacer(modifier.height(20.dp))

            StickyNoteCalendar(
                date = if (ui.stickyNoteDomain.dateTime == 0L) "Escolha uma data"
                else Instant.fromEpochMilliseconds(ui.stickyNoteDomain.dateTime).toLocalDateTime(
                    TimeZone.UTC
                ).date.format(kotlinx.datetime.LocalDate.Format {
                    byUnicodePattern("dd/MM/yyyy")
                }),

                isError = ui.erros.containsKey("date") to ui.erros["date"],
                onSelectedDate = { date ->
                    date?.let {
                        ui.stickyNoteDomain.copy(dateTime = date)
                        viewModel.validateFieldStickNote(
                            stickyNoteDomain = ui.stickyNoteDomain.copy(
                                dateTime = date,
                                name = lembreteName,
                                description = lembreteDescription
                            )
                        )
                        selectedDate = date
                    }
                }
            )

            StickNoteCheckBox(
                modifier = Modifier,
                stickyNoteDomain = ui.stickyNoteDomain,
                isChecked = { isChecked ->
                    isRemember = isChecked
                }
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = {
                        if(!isRemember){
                            createUpdateStickNote(
                                ui.stickyNoteDomain,
                                lembreteName,
                                lembreteDescription,
                                selectedDate,
                                isRemember,
                                tags,
                                onSave,
                            )
                            return@ElevatedButton
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.POST_NOTIFICATIONS) !=
                                PackageManager.PERMISSION_GRANTED) {
                                showRationale = true
                            }else{
                                createUpdateStickNote(
                                    ui.stickyNoteDomain,
                                    lembreteName,
                                    lembreteDescription,
                                    selectedDate,
                                    isRemember,
                                    tags,
                                    onSave,
                                )
                            }
                        }else{
                            createUpdateStickNote(
                                ui.stickyNoteDomain,
                                lembreteName,
                                lembreteDescription,
                                selectedDate,
                                isRemember,
                                tags,
                                onSave,
                            )
                        }
                    }) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                       text =  stringResource(R.string.salvar))
                }
            }
        }
    }
}

private fun createUpdateStickNote(
    stickyNoteDomain: StickyNoteDomain?,
    lembreteName: String,
    lembreteDescription: String,
    selectedDate: Long?,
    isRemeber: Boolean,
    tags: SnapshotStateList<String>,
    onSave: (StickyNoteDomain) -> Unit,
) {
    val stickNote = StickyNoteDomain(
        id = stickyNoteDomain?.id,
        name = lembreteName,
        description = lembreteDescription,
        dateTime = selectedDate!!,
        isRemember = isRemeber,
        noticafitionId = System.currentTimeMillis(),
        tags = tags
    )
    onSave(stickNote)
}

fun onActionAfterCreate(stickNote : StickyNoteDomain,context : Context) {
    if (stickNote.isRemember) {
        StickNoteAlarmManeger.criateAlarm(
            context = context,
            stickyNoteDomain = stickNote
        )
        return
    }
    StickNoteAlarmManeger.cancelNotification(context,stickNote)
}

@Preview
@Composable
private fun MyScreenPreview() {
    LembretesTheme {
        MyScreen(activity = Activity() , onSave = {}, viewModel = viewModel(), onClosed = {})
    }
}



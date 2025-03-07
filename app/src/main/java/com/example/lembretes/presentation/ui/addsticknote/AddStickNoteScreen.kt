package com.example.lembretes.presentation.ui.addsticknote

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.core.Constants
import com.example.lembretes.core.notification.StickNoteAlarmManeger
import com.example.lembretes.core.widgets.ShowAndCheckShouldRationale
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.DescriptionActivity
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteCheckBox
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteTagArea
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickyNoteCalendar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteAppBar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialogPerfil
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
    if (idStikcNote != 0) {
        SideEffect {
            addUpdateViewModel.findById(idStikcNote)
        }
    }


    MyScreen(
        onSave = if (idStikcNote != 0) addUpdateViewModel::updateStickNote
        else addUpdateViewModel::insertStickNote,
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
                messagem = "Para garantir que você receba lembretes importantes," +
                "este aplicativo precisa da permissão de notificações." +
                        "Isso nos permite exibir alertas sobre suas tarefas e " +
                        "compromissos no momento certo." +
                        "Conceder permissão?",
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
                    if (tags.size > 0) {
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
                                {
                                    if (ui.isSuccess) {
                                        onClosed()
                                    }
                                }
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
                                    {
                                        if (ui.isSuccess) {
                                            onClosed()
                                        }
                                    }
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
                                {
                                    if (ui.isSuccess) {
                                        onClosed()
                                    }
                                }
                            )
                        }
                        //TODO criar lógica para salvar o lembrete.verificar
                        // se necessaário notificação,lógica buscar lembretes que serão
                        // notificado,Alterar nome  do canal,Melhorar design da DescriptionActivity
                        /*createUpdateStickNote(
                            ui.stickyNoteDomain,
                            lembreteName,
                            lembreteDescription,
                            selectedDate,
                            isRemember,
                            tags,
                            onSave,
                            {
                                if (ui.isSuccess) {
                                     onClosed()
                                }
                            }
                        )*/
                    }) {
                    Text(stringResource(R.string.salvar))
                }
            }
        }
    }
}

@Composable
private fun verificationWhenPermissionDenied(
    modifier: Modifier,
    hasNotPermission: Boolean,
    onAccept : ()->Unit,
    onDenied : ()->Unit,
): Boolean {
    var hasNotPermission1 = hasNotPermission
    if (!hasNotPermission1) {
        StickNoteDialogPerfil(
            modifier = modifier,
            onDissmisRequest = onDenied,
            content = {
                Surface(
                    modifier = modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(size = 20.dp))
                        .background(
                            MaterialTheme.colorScheme.background,
                        ),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .clip(shape = RoundedCornerShape(30.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Permissão Necessária",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.permission_explication),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick =onDenied) {
                                Text(
                                    text = stringResource(R.string.negar),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            TextButton(onClick = onAccept) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.ok),
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        )
    }
    return hasNotPermission1
}


fun createAlarm(activity: Context) {
    val stickNoteAlarmManeger = StickNoteAlarmManeger()
    stickNoteAlarmManeger.criateAlarm(context = activity)
}

private fun createUpdateStickNote(
    stickyNoteDomain: StickyNoteDomain?,
    lembreteName: String,
    lembreteDescription: String,
    selectedDate: Long?,
    isRemeber: Boolean,
    tags: SnapshotStateList<String>,
    onSave: (StickyNoteDomain) -> Unit,
    onClosed: () -> Unit
) {
    val stickNote = StickyNoteDomain(
        id = stickyNoteDomain?.id,
        name = lembreteName,
        description = lembreteDescription,
        dateTime = selectedDate!!,
        isRemember = isRemeber,
        tags = tags
    )
    onSave(stickNote)
    onClosed()
}

fun verifyIfPermissionHasDenied(
    activity: Activity,
    permission: String,
    gerenciarPermissoes: ActivityResultLauncher<Array<String>>,
    listPemissions: Set<String>,
    context: Context
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    val hasPermission  = if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            )
            != PackageManager.PERMISSION_DENIED
        )  false else true
     if (!hasPermission){
         
     }
    }
}

@Preview
@Composable
private fun MyScreenPreview() {
    LembretesTheme {
        MyScreen(activity = Activity() , onSave = {}, viewModel = viewModel(), onClosed = {})
    }
}



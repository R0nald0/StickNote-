package com.example.lembretes.presentation.ui.addsticknote

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.core.notification.StickNoteAlarmManager
import com.example.lembretes.core.widgets.ShowAndCheckShouldRationale
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteCheckBox
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteTagArea
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickyNoteCalendar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteTextField
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.AddUpdateViewModel
import com.example.lembretes.presentation.viewmodel.GlobalVIewModel
import com.example.lembretes.utils.dateFormatToString
import com.google.gson.Gson
import kotlinx.datetime.format.FormatStringsInDatetimeFormats

@Composable
fun AddStickNoteScreen(
    modifier: Modifier = Modifier,
    globalVIewModel: GlobalVIewModel = viewModel(),
    stickNoteJson: String?,
    onClosed: () -> Unit,
    activity: Activity,
    onError: (String) -> Unit,
    onInfo: (String) -> Unit
) {

    val addUpdateViewModel = hiltViewModel<AddUpdateViewModel>()
    val state by addUpdateViewModel.addScreenUi.collectAsStateWithLifecycle()
    var stickNote by remember {
        mutableStateOf<StickyNoteDomain?>(null)
    }

    val context = LocalContext.current

    if (stickNoteJson != null && stickNoteJson.isNotEmpty()) {
        stickNote = Gson().fromJson(stickNoteJson, StickyNoteDomain::class.java)
    }

    if (state.error != null) {
        onError(state.error!!)
        addUpdateViewModel.clearErroMessage()
    }

    StickNoteLog.info("StickNote s $stickNote")

    MyScreen(
        viewModel = addUpdateViewModel,
        activity = activity,
        modifier = modifier,
        stickyNoteDomain = stickNote,
        onSave = { stickNote ->
            globalVIewModel.showLoader()
            if (stickNote.id != null) {
                updateStickNote(
                    addUpdateViewModel,
                    stickNote,
                    globalVIewModel,
                    context,
                    onInfo,
                    onClosed
                )
                return@MyScreen
            }

            insertStickNote(
                addUpdateViewModel,
                stickNote,
                globalVIewModel,
                context,
                onInfo,
                onClosed
            )
        }
    )
}

private fun updateStickNote(
    addUpdateViewModel: AddUpdateViewModel,
    stickNote: StickyNoteDomain,
    globalVIewModel: GlobalVIewModel,
    context: Context,
    onInfo: (String) -> Unit,
    onClosed: () -> Unit
) {

    addUpdateViewModel.updateStickNote(stickyNoteDomain = stickNote) { isSuccessFull ->
        globalVIewModel.hideLoader()
        if (!isSuccessFull) {
            return@updateStickNote
        }
        onActionAfterCreate(context = context, stickNote = stickNote)
        onInfo("Lembrete Atualizado")
        onClosed()
    }
}


private fun insertStickNote(
    addUpdateViewModel: AddUpdateViewModel,
    stickNote: StickyNoteDomain,
    globalVIewModel: GlobalVIewModel,
    context: Context,
    onInfo: (String) -> Unit,
    onClosed: () -> Unit
) {
    addUpdateViewModel.insertStickNote(stickyNoteDomain = stickNote) { isSuccessFull ->
        globalVIewModel.hideLoader()
        if (!isSuccessFull) {
            return@insertStickNote
        }
        onActionAfterCreate(context = context, stickNote = stickNote)
        onInfo("Lembrete Criado")
        onClosed()
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun MyScreen(
    onSave: (StickyNoteDomain) -> Unit,
    viewModel: AddUpdateViewModel,
    activity: Activity,
    stickyNoteDomain: StickyNoteDomain?,
    modifier: Modifier = Modifier,
) {
    val ui by viewModel.addScreenUi.collectAsStateWithLifecycle()
    val focus = LocalFocusManager.current

    var isRemember by rememberSaveable { mutableStateOf(stickyNoteDomain?.isRemember ?: false) }
    val tags = remember { mutableStateListOf<String>() }
    var tag by remember { mutableStateOf("") }

    var selectedDate: Long? by rememberSaveable {
        mutableStateOf(
            stickyNoteDomain?.dateTime
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .padding(16.dp)
            .fillMaxSize()
            .imePadding()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focus.clearFocus()
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        var lembreteName by rememberSaveable {
            mutableStateOf(
                stickyNoteDomain?.name ?: ""
            )
        }

        var lembreteDescription by rememberSaveable {
            mutableStateOf(stickyNoteDomain?.description ?: "")
        }

        Text(
            text = if (stickyNoteDomain?.id != null) stringResource(R.string.editar_lembrete)
            else stringResource(R.string.adicionar_lembrete)
        )
        Spacer(modifier = Modifier.height(10.dp))
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
            isError = ui.validateFields.containsKey("title"),
            onChange = { value ->
                lembreteName = value
            },
            singleLine = true,
            icon = {},
            trailingIcon = {
                if (ui.validateFields.containsKey("title")) {
                    Icon(
                        Icons.Outlined.Info, contentDescription = "Info icon",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            },
            supportTexting = {
                Text(ui.validateFields["title"] ?: "")
            }
        )

        StickNoteTextField(
            maxLines = 5,
            value = lembreteDescription,
            label = stringResource(R.string.descri_o),
            isError = ui.validateFields.containsKey("description"),
            onChange = { lembreteDescription = it },
            singleLine = false,
            icon = { },
            trailingIcon = {},
            supportTexting = {
                Text(ui.validateFields["description"] ?: "")
            }
        )

        Spacer(modifier.height(15.dp))

        LaunchedEffect(key1 = stickyNoteDomain?.tags?.isNotEmpty()) {
            val stickyNoteDomain = stickyNoteDomain
            if (stickyNoteDomain?.tags?.isNotEmpty() == true) {
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
            date = if (stickyNoteDomain == null || stickyNoteDomain.dateTime == 0L) "Escolha uma data"
            else stickyNoteDomain.dateTime.dateFormatToString(),
            isError = ui.validateFields.containsKey("date") to ui.validateFields["date"],
            onSelectedDate = { dateLong ->
                focus.clearFocus()
                dateLong?.let {
                    stickyNoteDomain?.copy(
                        dateTime = dateLong,
                        name = lembreteName,
                        description = lembreteDescription
                    )

                    viewModel.validateFieldStickNote(
                        dateTime = dateLong, description = lembreteDescription, name = lembreteName
                    )
                    selectedDate = dateLong
                }
            }
        )

        StickNoteCheckBox(
            modifier = Modifier,
            stickyNoteDomain = stickyNoteDomain,
            isChecked = { isChecked ->
                isRemember = isChecked
            }
        )

        SaveNoteWithPermission(
            activity = activity,
            tags = tags,
            lembreteName = lembreteName,
            onSave = onSave,
            lembreteDescription = lembreteDescription,
            selectedDate = selectedDate,
            isRemember = isRemember,
            stickyNoteDomain = stickyNoteDomain,
            modifier = modifier
        )
    }
}

@Composable
fun SaveNoteWithPermission(
    modifier: Modifier = Modifier,
    activity: Activity,
    stickyNoteDomain: StickyNoteDomain?,
    lembreteName: String,
    lembreteDescription: String,
    selectedDate: Long?,
    isRemember: Boolean,
    tags: SnapshotStateList<String>,
    onSave: (StickyNoteDomain) -> Unit
) {

    var showRationale by remember { mutableStateOf(false) }
    var permissionsNotAllowed by remember { mutableStateOf("") }
    var onActionIfShouldRationale by remember { mutableStateOf<(() ->Unit)?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (permission, granted) ->
            StickNoteLog.info("CHECK PERMISSION: $permission:$granted")
            if (!granted) {
                permissionsNotAllowed = permission
                showRationale = true
            }
        }
    }

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
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            activity,Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        ContextCompat.getSystemService(activity, AlarmManager::class.java).apply {
                             if ( this!!.canScheduleExactAlarms()){
                                 createUpdateStickNote(
                                     stickyNoteDomain,
                                     lembreteName,
                                     lembreteDescription,
                                     selectedDate,
                                     isRemember,
                                     tags,
                                     onSave,
                                 )
                                 return@apply
                             }else{
                                 launcher.launch(arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM))
                                 onActionIfShouldRationale = {
                                     val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                                     activity.startActivity(intent)
                                 }
                             }
                        }

                    } else {
                        launcher.launch(
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                        )
                    }
                } else {
                    createUpdateStickNote(

                        stickyNoteDomain,
                        lembreteName,
                        lembreteDescription,
                        selectedDate,
                        isRemember,
                        tags,
                        onSave,
                    )
                }
            }
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = stringResource(R.string.salvar)
            )
        }
    }


    if (showRationale) {
        ShowAndCheckShouldRationale(
            modifier = modifier,
            activity = activity,
            permission = permissionsNotAllowed,
            onCancel = { showRationale = false },
            onAccept = {
                showRationale = false
                onActionIfShouldRationale?.invoke()
            },
        )
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
        dateTime = selectedDate ?: 0,
        isRemember = isRemeber,
        noticafitionId = System.currentTimeMillis(),
        tags = tags
    )

    onSave(stickNote)
}

fun onActionAfterCreate(stickNote: StickyNoteDomain, context: Context){
    if (stickNote.isRemember) {
        StickNoteAlarmManager.criateAlarm(
            context = context,
            stickyNoteDomain = stickNote,
        )
        return
    }
    StickNoteAlarmManager.cancelNotification(context, stickNote)

}

@Preview
@Composable
private fun MyScreenPreview() {
    LembretesTheme {}
}



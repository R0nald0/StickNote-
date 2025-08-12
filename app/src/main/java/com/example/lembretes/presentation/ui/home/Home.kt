package com.example.lembretes.presentation.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.rememberIntroShowcaseState
import com.example.lembretes.R
import com.example.lembretes.core.notification.StickNoteAlarmManager
import com.example.lembretes.core.widgets.ShowAndCheckShouldRationale
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.home.widgets.IntroStickNoteBoxExample
import com.example.lembretes.presentation.ui.home.widgets.MenuNavStickNote
import com.example.lembretes.presentation.ui.home.widgets.StateListStickNote
import com.example.lembretes.presentation.ui.home.widgets.StickNoteToolBar
import com.example.lembretes.presentation.ui.shared.widgets.ContentDialog
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog
import com.example.lembretes.presentation.viewmodel.GlobalVIewModel
import com.example.lembretes.presentation.viewmodel.PreferencesViewModel
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import com.example.lembretes.utils.getDateCurrentSystemDefaultInLocalDateTime
import com.example.lembretes.utils.getDateFromLongOfCurrentSystemDate
import kotlinx.datetime.Clock


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    context: Activity,
    globalVIewModel: GlobalVIewModel = viewModel(),
    stickNoteViewModel: StickNoteViewmodel = viewModel(),
    userViewModel: UserViewModel = viewModel<UserViewModel>(),
    prefViewModel: PreferencesViewModel = viewModel<PreferencesViewModel>(),
    onUpdate: (StickyNoteDomain) -> Unit,
    onNavigateToAddStickNote: () -> Unit,
    openSearch: () -> Unit,
    onErrorMessage :(String) -> Unit,
    onInfoMessage :(String) -> Unit
) {

    var permission by remember { mutableStateOf("") }
    var onActionIfShouldRationale by remember { mutableStateOf<(()->Unit)?>( null) }
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val uiState by stickNoteViewModel.uiState.collectAsStateWithLifecycle()
    var showRationale by remember { mutableStateOf(false) }
    val introShowCaseState  = rememberIntroShowcaseState()
    val pref by prefViewModel.userPreference.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.map {
            if (!it.value) {
                permission = it.key
                showRationale = true
            }
        }
    }

    var showPerfilDialog by remember {
        mutableStateOf(false)
    }
    var showInfoIntroCardLembrete by remember { mutableStateOf(false)}

     if (showPerfilDialog) {
        StickNoteDialog(
            content = {
                ContentDialog(
                    user = user,
                    onDismissRequest = { showPerfilDialog = !showPerfilDialog },
                    onSave = { name, photoPath ->
                        userViewModel.crateUser(
                            name = name,
                            urlPerfilPhoto = photoPath
                        ) { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                )
            },
            onDissmisRequest = { showPerfilDialog = !showPerfilDialog },
        )
    }
     if (uiState.error != null){
         onErrorMessage(uiState.error!!)
         stickNoteViewModel.clearErroMessage()
     }

    if (showInfoIntroCardLembrete){
        BasicAlertDialog(
            onDismissRequest = {},
            modifier = Modifier.fillMaxWidth()
                .clip(shape = RoundedCornerShape(15.dp)),
            content = {
                IntroStickNoteBoxExample(
                    onHide = {
                        prefViewModel.updateShowIntroApp(false)
                        showInfoIntroCardLembrete = false
                    }
                )
            })
    }

    IntroShowcase(
        showIntroShowCase = pref.showIntroApp,
        state = introShowCaseState,
        onShowCaseCompleted = {
            showInfoIntroCardLembrete = true
        }

    ) {
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            StickNoteToolBar(
                user = user,
                modifier = Modifier,
                numberOfStickNotes = uiState.scheduledReminders,
                onOpenProfile = { showPerfilDialog = !showPerfilDialog },
                openSearch = openSearch
            )
            MenuNavStickNote(
                modifier = modifier,
                uiState = uiState,
                onFilterType = { filterType ->
                    stickNoteViewModel.alterFilterType(filterType)
                }
            )

            StateListStickNote(
                uiState = uiState,
                modifier = Modifier,
                onNavigateToAddStickNote = onNavigateToAddStickNote,
                onUpdate = onUpdate,
                context = context,
                onDelete = { stickNote ->
                    stickNoteViewModel.deleteStickNote(stickyNoteDomain = stickNote) {
                        if (stickNote.isRemember) {
                            StickNoteAlarmManager.cancelNotification(context, stickNote)
                        }
                    }
                },
                onUpdateStateNotificaion = { stickNoteUp ,isRemember ->
                    if (stickNoteUp == null  || stickNoteUp.id == null
                        || Clock.System.getDateFromLongOfCurrentSystemDate(stickNoteUp.dateTime)  <=
                        Clock.System.getDateCurrentSystemDefaultInLocalDateTime()
                    ) {
                        onInfoMessage( context.getString(R.string.lembrete_com_a_data_inv_lida))
                        return@StateListStickNote
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) ==
                            PackageManager.PERMISSION_GRANTED
                        ) {
                            val alarm  = context.getSystemService(AlarmManager::class.java)

                            if (!alarm.canScheduleExactAlarms()){
                                launcher.launch(
                                    arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM)
                                )
                                onActionIfShouldRationale = {
                                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                                    context.startActivity(intent)
                                }

                                return@StateListStickNote
                            }else{
                                globalVIewModel.showLoader()
                                stickNoteViewModel.updateNotificatioStickNote(stickNoteUp.copy(isRemember = isRemember)) {
                                    globalVIewModel.hideLoader()
                                    checkCreateCancelNotification(
                                        isRemember,
                                        context,
                                        stickNoteUp
                                    )
                                }
                            }
                        } else {
                            launcher.launch(
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                            )
                        }
                    } else {
                        globalVIewModel.showLoader()
                        stickNoteViewModel.updateNotificatioStickNote(stickNoteUp.copy(isRemember = isRemember)) { message ->
                            globalVIewModel.hideLoader()
                            if (message != null) {
                                onInfoMessage(message)
                                return@updateNotificatioStickNote
                            }
                            checkCreateCancelNotification(
                                isRemember,
                                context,
                                stickNoteUp,
                            )
                        }
                    }
                }
            )
        }
    }


    if (showRationale){
        ShowAndCheckShouldRationale(
            modifier = modifier,
            activity = context,
            permission = permission,
            onCancel = {
                permission= ""
                showRationale = false
            },
            onAccept = {
                permission= ""
                onActionIfShouldRationale?.invoke()
                showRationale = false
            }
        )
    }

}

private fun checkCreateCancelNotification(
    isRemember: Boolean,
    context: Activity,
    stickNote: StickyNoteDomain,
) {
    if (isRemember) StickNoteAlarmManager.criateAlarm(context, stickNote)
    else StickNoteAlarmManager.cancelNotification(context, stickNote)
}









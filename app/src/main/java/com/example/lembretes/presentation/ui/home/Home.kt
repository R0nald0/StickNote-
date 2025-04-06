package com.example.lembretes.presentation.ui.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.core.notification.StickNoteAlarmManeger
import com.example.lembretes.core.notification.StickNoteSnackBar
import com.example.lembretes.core.notification.StickNoteSnackBarInfo
import com.example.lembretes.core.widgets.ShowAndCheckShouldRationale
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.home.widgets.MenuNavStickNote
import com.example.lembretes.presentation.ui.home.widgets.StateListStickNote
import com.example.lembretes.presentation.ui.home.widgets.StickNoteDrawer
import com.example.lembretes.presentation.ui.home.widgets.StickNoteToolBar
import com.example.lembretes.presentation.ui.shared.widgets.ContentDialog
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    context: Activity,
    stickNoteViewModel: StickNoteViewmodel = viewModel(),
    modifier: Modifier = Modifier,
    onUpdate: (StickyNoteDomain) -> Unit,
    onNavigateToAddStickNote: () -> Unit,
    onNavigateToSettingsScreen: () -> Unit,
    openSearch: () -> Unit
) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackBarHots = remember { SnackbarHostState() }

    val uiState by stickNoteViewModel.uiState.collectAsStateWithLifecycle()

    var showRationale by remember {
        mutableStateOf(false)
    }
    val lauche = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.map {
            if (!it.value) {
                ContextCompat.checkSelfPermission(context, it.key)
                Log.i("INFO", "onCreate: ${it.key}:${it.value} ")
            }
        }
    }

    var showPerfilDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            lauche.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            )
        }
    }
    if (showMessage) {
        StickNoteSnackBarInfo(message = context.getString(R.string.lembrete_com_a_data_inv_lida)) {}
        showMessage = false
    }
    if (showRationale) {
        ShowAndCheckShouldRationale(
            modifier = modifier,
            activity = context,
            permission = Manifest.permission.POST_NOTIFICATIONS,
            messagem = stringResource(R.string.permission_explication),
            onCancel = { showRationale = false },
            onAccept = {
                showRationale = false
                lauche.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            }
        )
    }

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

    StickNoteDrawer(
        modifier = modifier,
        user = user,
        drawerState = drawerState,
        onClickMenu = {},
        onNavigateToSettingsScreen = onNavigateToSettingsScreen,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center,
        ) {
            Scaffold(
                topBar = {
                    StickNoteToolBar(
                        isOpenDrawer = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        user = user,
                        modifier = modifier,
                        numberOfStickNotes = uiState.scheduledReminders,
                        onOpenProfile = { showPerfilDialog = !showPerfilDialog },
                        openSearch = openSearch
                    )
                },
                snackbarHost = {
                    StickNoteSnackBar(
                        snackBarHots,
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToAddStickNote
                    ) {
                        Icon(
                            Icons.Default.Add, contentDescription = "button add new stick Note",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
            ) { paddingValues ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(paddingValues)
                ) {

                    MenuNavStickNote(
                        modifier = modifier,
                        uiState = uiState,
                        onFilterType = { filterType ->
                            stickNoteViewModel.alterFilterType(filterType)
                        }
                    )

                    StateListStickNote(
                        uiState = uiState,
                        modifier = modifier,
                        onNavigateToAddStickNote = onNavigateToAddStickNote,
                        onUpdate = onUpdate,
                        context = context,
                        onDelete = { stickNote ->
                            stickNoteViewModel.deleteStickNote(stickyNoteDomain = stickNote) {
                                if (stickNote.isRemember) {
                                    StickNoteAlarmManeger.cancelNotification(context, stickNote)
                                }
                            }
                        },
                        onUpdateStateNotificaion = { stickNote ->
                            if (stickNote == null) {
                                scope.launch {
                                    snackBarHots.showSnackbar(
                                        message = context.getString(R.string.lembrete_com_a_data_inv_lida)
                                    )
                                }
                                return@StateListStickNote
                            }
                            val (id, _, _, _, isRemember) = stickNote

                            if (id == null) {
                                Toast.makeText(
                                    context,
                                    getString(context, R.string.falha_ao_criar_notifica_o),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@StateListStickNote
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) !=
                                    PackageManager.PERMISSION_GRANTED
                                ) {
                                    showRationale = true
                                } else {
                                    stickNoteViewModel.updateNotificatioStickNote(stickNote) {
                                        checkCreateCancelNotification(
                                            isRemember,
                                            context,
                                            stickNote
                                        )
                                    }
                                }
                            } else {
                                stickNoteViewModel.updateNotificatioStickNote(stickNote) { messager ->
                                    if (messager != null) {
                                        Toast.makeText(context, messager, Toast.LENGTH_LONG).show()
                                        return@updateNotificatioStickNote
                                    }
                                    checkCreateCancelNotification(
                                        isRemember,
                                        context,
                                        stickNote,
                                    )
                                }
                            }
                        }
                    )
                }

                if (uiState.isLoading) {
                    StickNoteDialog(
                        modifier
                            .fillMaxSize()
                            .background(color = Color.Gray.copy(alpha = 0.6f)),
                        onDissmisRequest = {},
                        content = {
                            CircularProgressIndicator()
                        }
                    )

                }
            }
        }
    }

}

private fun checkCreateCancelNotification(
    isRemember: Boolean,
    context: Activity,
    stickNote: StickyNoteDomain,
) {
    if (isRemember) {
        StickNoteAlarmManeger.criateAlarm(context, stickNote)
    } else {
        //TODO verificar dando erro de não registrado
        StickNoteAlarmManeger.cancelNotification(context, stickNote)
    }
}


@Composable
internal fun StickNoteNoContent(
    modifier: Modifier,
    filterType: StickNoteEnumFilterType,
    onNavigateToAddStickNote: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Você ainda não adicionou nenhum lembrete ${getTextNameSearch(filterType)}.",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = {
                onNavigateToAddStickNote()
            }) {
            Text(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = "Criar lembrete",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


private fun getTextNameSearch(type: StickNoteEnumFilterType): String {
    return when (type) {
        StickNoteEnumFilterType.Today -> "para hoje"
        StickNoteEnumFilterType.TOMORROW -> "para amanhã"
        else -> ""
    }
}



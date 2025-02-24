package com.example.lembretes.presentation.ui.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.User
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.home.widgets.MenuNavStickNote
import com.example.lembretes.presentation.ui.home.widgets.StateListStickNote
import com.example.lembretes.presentation.ui.home.widgets.StickNoteDrawer
import com.example.lembretes.presentation.ui.home.widgets.StickNoteToolBar
import com.example.lembretes.presentation.ui.shared.widgets.ContentDialog
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialogPerfil
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch


data class HomeState(
    val listData :List<StickyNoteDomain>? =null,
    val user : User = User(),
    val filterType: StickNoteEnumFilterType =StickNoteEnumFilterType.Today,
    val scheduledReminders : Int= 0,
    val isLoading : Boolean = false,
    val erro :String? =null
)

@Composable
 fun HomeScreen(
    context : Context,
    stickNoteViewModel : StickNoteViewmodel = viewModel(),
    modifier: Modifier = Modifier,
    onUpdate:(StickyNoteDomain)->Unit,
    onNavigateToAddStickNote : ()->Unit,
    onNavigateToSettingsScreen :()->Unit,
    openSearch :()-> Unit
) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by stickNoteViewModel.uiState.collectAsStateWithLifecycle()


    var showPerfilDialog by remember{
        mutableStateOf(false)
    }

    if (showPerfilDialog){
        StickNoteDialogPerfil(
            content ={
                ContentDialog(
                    user = uiState.user,
                    onDismissRequest = { showPerfilDialog = !showPerfilDialog},
                    onSave = userViewModel::crateUser
                )
            },
            onDissmisRequest = { showPerfilDialog = !showPerfilDialog },
        )
    }

  StickNoteDrawer(
      modifier = modifier,
      user = uiState.user,
      drawerState = drawerState,
      onClickMenu = {},
      onNavigateToSettingsScreen = onNavigateToSettingsScreen,
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
                  user = uiState.user,
                  modifier = modifier,
                  numberOfStickNotes = uiState.scheduledReminders,
                  onOpenProfile = {showPerfilDialog = !showPerfilDialog},
                  openSearch = openSearch
              )
          },
          floatingActionButton = {
              FloatingActionButton(
                  containerColor = MaterialTheme.colorScheme.inversePrimary,
                  onClick =onNavigateToAddStickNote
              ) {
                  Icon(Icons.Default.Add, contentDescription ="button add new stick Note" )
              }
          },
      ) {paddingValues ->
          Column(
              modifier = modifier
                  .fillMaxSize()
                  .navigationBarsPadding()
                  .padding(paddingValues)
          ) {

             MenuNavStickNote(
                 modifier =modifier,
                 uiState = uiState,
                 onFilterType = {
                     filterType -> stickNoteViewModel.alterFilterType(filterType)
                  }


             )

              StateListStickNote(
                  uiState =   uiState,
                  modifier =  modifier,
                  onNavigateToAddStickNote  =onNavigateToAddStickNote,
                  onUpdate = onUpdate,
                  context =  context,
                  onDelete = stickNoteViewModel::deleteStickNote,
                  onUpdateStateNotificaion= stickNoteViewModel::updateNotificatioStickNote
              )
          }

      }
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
            .padding(8.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Você ainda não adicionou nenhum lembrete ${getTextNameSearch(filterType)}.",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(onClick = {
            onNavigateToAddStickNote()
        }) {
            Text(
                text = "Criar lembrete",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}




private fun getTextNameSearch(type: StickNoteEnumFilterType):String{
    return when(type){
        StickNoteEnumFilterType.Today -> "para hoje"
        StickNoteEnumFilterType.TOMORROW->"para amanhã"
        else ->""
    }
}



package com.example.lembretes.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.User
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.home.widgets.StickNoteBoxSwipToDismiss
import com.example.lembretes.presentation.ui.home.widgets.StickNoteDrawer
import com.example.lembretes.presentation.ui.shared.widgets.LoadingScreen
import com.example.lembretes.presentation.ui.shared.widgets.StickChips
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteCardView
import com.example.lembretes.presentation.ui.shared.widgets.SticnkNoteToolBar
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.util.Calendar


data class HomeState(
    val listData :List<StickyNoteDomain>? =null,
    val user : User = User(),
    val filterType: StickNoteEnumFilterType =StickNoteEnumFilterType.Today,
    val scheduledReminders : Int= 0,
    val isLoading : Boolean = false,
    val erro :String? =null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun HomeScreen(
    context : Context,
    userViewModel: UserViewModel = viewModel(),
    stickNoteViewModel : StickNoteViewmodel = viewModel(),
    modifier: Modifier = Modifier,
    onUpdateStateNotificaion : (Int,Boolean)->Unit,
    onUpdate:(StickyNoteDomain)->Unit,
    onDelete: (StickyNoteDomain) -> Unit,
    onNavigateToAddStickNote : ()->Unit,
    onNavigateToSettingsScreen :()->Unit,
    openSearch :()-> Unit
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scroolBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val uiState by stickNoteViewModel.uiState.collectAsStateWithLifecycle()
    val user by userViewModel.user.collectAsStateWithLifecycle()

    var showPerfilDialog by remember{
        mutableStateOf(false)
    }

  StickNoteDrawer(
      modifier = modifier,
      user = user,
      drawerState = drawerState,
      showDialog = showPerfilDialog,
      onCrateUser = userViewModel::crateUser,
      onClickMenu = {},
      onNavigateToSettingsScreen = onNavigateToSettingsScreen,
  ) {
      Scaffold(
          modifier = modifier.background(MaterialTheme.colorScheme.background),
          topBar = {
              SticnkNoteToolBar(
                  isOpenDrawer = {
                      scope.launch {
                          drawerState.apply {
                              if (isClosed) open() else close()
                          }
                      }
                  },
                  scroolBehavior = scroolBehavior,
                  modifier = modifier,
                  title = if (scroolBehavior.state.collapsedFraction <= 0.7)
                      "Olá ${user.name.capitalize(Locale.current)},${daySection()}!!"
                  else "Lembrete" ,
                  isColapsed = if(scroolBehavior.state.collapsedFraction <= 0.7f) true else false ,
                  numberOfStickNotes = uiState.scheduledReminders,
                  onOpenProfile = {showPerfilDialog = !showPerfilDialog},
                  openSearch = openSearch
              )
          },
          floatingActionButton = {
              FloatingActionButton(onClick =onNavigateToAddStickNote) {
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

              MenuNavStickNote(modifier,uiState, stickNoteViewModel)

              StateListStickNote(
                  uiState =   uiState,
                  modifier =  modifier,
                  onNavigateToAddStickNote  =onNavigateToAddStickNote,
                  onUpdate = onUpdate,
                  context =  context,
                  onDelete = onDelete,
                  onUpdateStateNotificaion= onUpdateStateNotificaion
              )
          }

      }
  }

}
@Composable
private fun MenuNavStickNote(
    modifier: Modifier,
    uiState: HomeState,
    viewModel: StickNoteViewmodel
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StickChips(
            label = StickNoteEnumFilterType.Today.value,
            isSelected = uiState.filterType == StickNoteEnumFilterType.Today,
            onClick = {
                uiState.copy(isLoading = true)
                viewModel.alterFilterType(StickNoteEnumFilterType.Today)
            },
        )
        Spacer(modifier = Modifier.width(10.dp))
        StickChips(label = StickNoteEnumFilterType.TOMORROW.value,
            isSelected = uiState.filterType == StickNoteEnumFilterType.TOMORROW,
            onClick = {
                    viewModel.alterFilterType(StickNoteEnumFilterType.TOMORROW)
                    uiState.copy(isLoading = true)
            })
        Spacer(modifier = Modifier.width(10.dp))
        StickChips(label = StickNoteEnumFilterType.All.value,
            isSelected = uiState.filterType == StickNoteEnumFilterType.All,
            onClick = {
                    viewModel.alterFilterType(StickNoteEnumFilterType.All)
                    uiState.copy(isLoading = true)
            }
        )
    }
}

@Composable
private fun StateListStickNote(
    uiState: HomeState,
    modifier: Modifier,
    onNavigateToAddStickNote: () -> Unit,
    onUpdate: (StickyNoteDomain) -> Unit, //vefificar aqui
    context: Context,
    onDelete: (StickyNoteDomain) -> Unit,
    onUpdateStateNotificaion: (Int, Boolean) -> Unit
) {
    when {
        uiState.listData != null -> {
            val listStickNote = uiState.listData
            if (listStickNote.isEmpty()) {
               StickNoteNoContent(modifier, uiState.filterType, onNavigateToAddStickNote)
            } else {

                StickNoteStateLazyList(
                    stickNotes = listStickNote,
                    context = context,
                    onNavigateToAddStickNote = onUpdate,
                    onDelete = onDelete,
                    onUpdateStateNotificaion = onUpdateStateNotificaion
                )
            }
        }

        uiState.isLoading -> {
            LoadingScreen(isLoading = { uiState.isLoading }, loading = uiState.isLoading )
        }

        uiState.erro != null -> {
            Toast.makeText(context, uiState.erro, Toast.LENGTH_SHORT).show()
        }

    }
}

@Composable
private fun StickNoteNoContent(
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

@Composable
fun StickNoteStateLazyList(
    modifier: Modifier = Modifier,
    context: Context,
    stickNotes: List<StickyNoteDomain>,
    onNavigateToAddStickNote: (StickyNoteDomain) ->Unit,
    onDelete:(StickyNoteDomain)->Unit,
    onUpdateStateNotificaion : (Int,Boolean)->Unit,
    ) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(stickNotes) { stickNote ->
            Log.i("_INFO", "card")

            val switToDismessState = rememberSwipeToDismissBoxState()

            LaunchedEffect(key1 = switToDismessState.currentValue) {
                when (switToDismessState.currentValue) {
                    SwipeToDismissBoxValue.StartToEnd -> onNavigateToAddStickNote(stickNote)
                    SwipeToDismissBoxValue.EndToStart -> onDelete(stickNote)
                    SwipeToDismissBoxValue.Settled -> {}
                }
            }

            MySwippe(
                modifier = modifier,
                onUpdateStateNotificaion = onUpdateStateNotificaion,
                stickNote = stickNote,
                context = context,
                dismissState = switToDismessState
            )
        }
    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MySwippe(
    modifier : Modifier = Modifier,
    onUpdateStateNotificaion : (Int,Boolean)->Unit,
    dismissState: SwipeToDismissBoxState,
    stickNote :StickyNoteDomain,
    context :Context,
){
    var color  = Color.Transparent
    var align  =Arrangement.End
    var icon = R.drawable.delete_24
    var textTitle = ""

    when(dismissState.dismissDirection ){
        SwipeToDismissBoxValue.EndToStart->{
            color = Color.Red
            align = Arrangement.End
            textTitle = "Deletar"
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            color = Color.Green
            align = Arrangement.Start
            textTitle = "Atualizar"
            icon = R.drawable.baseline_mode_edit_24

            LaunchedEffect(dismissState.currentValue) {
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }
        SwipeToDismissBoxValue.Settled -> {}

    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = dismissState ,
        backgroundContent ={
            StickNoteBoxSwipToDismiss(
                contentAlignment = align,
                backGroundcolor =color ,
                icon = icon,
                textTitle =textTitle
            )
        } ) {
        StickNoteCardView(
            stickyNoteDomain =  stickNote,
            onUpdateStateNotificaion = onUpdateStateNotificaion,
            modifier = modifier ,
            context = context
        )
    }
}


private fun getTextNameSearch(type: StickNoteEnumFilterType):String{
    return when(type){
        StickNoteEnumFilterType.Today -> "para hoje"
        StickNoteEnumFilterType.TOMORROW->"para amanhã"
        else ->""
    }
}
private fun daySection() : String{
    val calendar =  Calendar.getInstance()
    val hour =  calendar.get(Calendar.HOUR_OF_DAY)
   return when(hour){
        in 0..5-> "Boa noite"
        in 6..11->"Bom Dia"
        in 12..17->"Boa Tarde"
        in 18..24 -> "Boa noite"
       else -> ""
   }
}


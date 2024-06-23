package com.example.lembretes.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.User
import com.example.lembretes.presentation.model.NavigationItemDataClass
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.theme.Blue70
import com.example.lembretes.presentation.ui.widgets.LoadingScreen
import com.example.lembretes.presentation.ui.widgets.StickChips
import com.example.lembretes.presentation.ui.widgets.StickNoteCardView
import com.example.lembretes.presentation.ui.widgets.StickNoteDialogPerfil
import com.example.lembretes.presentation.ui.widgets.SticnkNoteToolBar
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import com.example.lembretes.utils.dateForExtense
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


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
    userViewModel: UserViewModel,
    viewModel : StickNoteViewmodel,
    modifier: Modifier = Modifier,
    onUpdateStateNotificaion : (Int,Boolean)->Unit,
    onDelete: (StickyNoteDomain) -> Unit,
    onNavigateToAddStickNote : ()->Unit,
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scroolBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user by userViewModel.user.collectAsStateWithLifecycle()

    var showPerfilDialog by remember{
        mutableStateOf(false)
    }

    if (showPerfilDialog){
        StickNoteDialogPerfil(
            user = user,
            onDissmisRequest = { showPerfilDialog = !showPerfilDialog },
            onSave = userViewModel::crateUser
        )
    }

    ModalNavigationDrawer(
        drawerState=drawerState,
        drawerContent = {
            StickNoteDrawer(
                openDialog = {showPerfilDialog = !showPerfilDialog},
                onCloseDrawer = {scope.launch { drawerState.close() }},
            ) },
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

                    MenuNavStickNote(modifier,uiState, viewModel)

                    StateListStickNote(
                        uiState,
                        modifier,
                        onNavigateToAddStickNote,
                        context,
                        onDelete,
                        onUpdateStateNotificaion
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
                    onNavigateToAddStickNote = onNavigateToAddStickNote,
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
private fun StickNoteDrawer(
    modifier: Modifier = Modifier,
    today : String =Date().dateForExtense(),
    onCloseDrawer : ()->Unit,
    openDialog:() ->Unit
) {

    ModalDrawerSheet(
        modifier = modifier.fillMaxWidth(fraction = 0.7f),
    )
    {

        Spacer(modifier = modifier.height(10.dp))
        Surface(
            modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.2f)
        ) {
            Row(
                modifier
                    .fillMaxSize()
                    .background(color = Blue70),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text =today,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
//                         Icon(
//                              painter = painterResource(id = R.drawable.sun),
//                             contentDescription = "sun",
//                             modifier = modifier.size(60.dp),
//                             tint = Color.Yellow
//                         )
            }
        }

        HorizontalDivider()
        Spacer(modifier = modifier.height(10.dp))
        navigationsItems().forEach {itemNav->
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "User icons") },
                label = { Text(text = itemNav.label) },
                selected = itemNav.selected,
                onClick ={ onCloseDrawer()
                           openDialog()
                         },
                badge = { Text(itemNav.badge) }
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickNoteStateLazyList(
    modifier: Modifier = Modifier,
    context: Context,
    stickNotes: List<StickyNoteDomain>,
    onNavigateToAddStickNote: () ->Unit,
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
                    SwipeToDismissBoxValue.StartToEnd -> onNavigateToAddStickNote()
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
@OptIn(ExperimentalMaterial3Api::class)
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
            BoxSwipToDismis(contentAlignment = align, backGroundcolor =color , icon = icon,textTitle =textTitle )
        } ) {
        StickNoteCardView(
            stickyNoteDomain =  stickNote,
            onUpdateStateNotificaion = onUpdateStateNotificaion,
            modifier = modifier ,
            context = context
        )
    }
}

@Composable
fun BoxSwipToDismis(
    contentAlignment: Arrangement.Horizontal,
    backGroundcolor : Color,
    icon : Int,
    textTitle : String
) {
    Box(
        modifier = Modifier.clip(CircleShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(backGroundcolor)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = contentAlignment
        ) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(id = icon), contentDescription = "" , tint = Color.White)
            }
            Text(
                text = textTitle,
                color  = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun navigationsItems(
):List<NavigationItemDataClass>{
    return  listOf(
        NavigationItemDataClass(
            icon =  Icons.Filled.AccountCircle,
            label = "Perfil",
            selected = false,
            onClick = {},
            badge =  ""
        ),
        NavigationItemDataClass(
            icon = Icons.Filled.Settings,
            label = "Configurações",
            selected = false,
            onClick = { },
            badge = ""
        ),
    )
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


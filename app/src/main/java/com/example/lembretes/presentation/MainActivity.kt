package com.example.lembretes.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.R
import com.example.lembretes.core.notification.showNotification
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.ui.widgets.MyCardView
import com.example.lembretes.presentation.ui.widgets.StickChips
import com.example.lembretes.presentation.viewmodel.StickNoteState
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.utils.convertDateLongToString
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private  val viewModel by  viewModels<StickNoteViewmodel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LembretesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ObserveModel(viewModel = viewModel, context = this)
                }
            }
        }
    }

}

fun dateForExtense() :String{
    val date  = Date()
    val locale = Locale("pt","BR")
    val cl = GregorianCalendar.getInstance(locale)
    val d  = cl.get(GregorianCalendar.DAY_OF_WEEK)
    val t =  cl.get(GregorianCalendar.WEEK_OF_MONTH)
    val format  =SimpleDateFormat( "'${geetDayOfWeek(d)}',dd MMMM,yyyy ",locale)
    return  format.format(date.time)
}

fun geetDayOfWeek(day : Int):String{
  return  when(day){
        1->"Domingo "
        2->"Segunda-Feira "
        3->"Terça-Feira "
        4->"Quarta-Feira "
        5->"Quinta-Feira "
        6->"Sexta-Feira "
        7->"Sabado"
      else -> ""
  }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
     stickyNoteList : List<StickyNoteDomain>,
     onUpdateStateNotificaion : (StickyNoteDomain)->Unit ,
     modifier: Modifier = Modifier ,
     onDelete:(StickyNoteDomain)->Unit ,
     context: Context
) {
     val scroolBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
     var isSelected by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),

        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                scrollBehavior = scroolBehavior,
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon( Icons.Default.AccountCircle, contentDescription ="User avatar" )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon( Icons.Default.Menu, contentDescription ="User menu" )
                    }
                },

                title = {
                    Column(
                    ) {

                        Log.i("INFO_", "MyApp: $scroolBehavior")
                        Text(
                            text = "Olá Dev,Bom Dia"
                        )
                        Text(
                            text = "voce tem  4 lembretes agendados",
                            fontSize = 13.sp,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
                         FloatingActionButton(onClick = {
                             val intent = Intent(context,AddStickNoteActivity::class.java)
                             context.startActivity(intent )
                         }) {
                             Icon(Icons.Default.Add, contentDescription ="button add new stick Note" )
                         }      
        },
    
    ) {paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
             Row(
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 StickChips(
                     label = dateForExtense(),
                     isSelected = true,
                 )
                 Spacer(modifier = modifier.fillMaxWidth(fraction = 0.1f))
                 StickChips(label = "Ter" ,isSelected = isSelected)
                 Spacer(modifier = modifier.fillMaxWidth(fraction = 0.1f))
                 StickChips(label = "Qua", isSelected = isSelected)
                 Spacer(modifier = modifier.fillMaxWidth(fraction = 0.1f))
                 StickChips(label = "Qui")
                 Spacer(modifier = modifier.fillMaxWidth(fraction = 0.1f))
                 StickChips(label = "Sex")
             }

            if (stickyNoteList.isNotEmpty()) {

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(stickyNoteList) { stickNote ->

                        val switToDismessState = rememberSwipeToDismissBoxState()
                        when (switToDismessState.currentValue) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                val intent = Intent(
                                    context.applicationContext,
                                    AddStickNoteActivity::class.java
                                )
                                intent.putExtra("stick", stickNote)
                                context.startActivity(intent)
                            }

                            SwipeToDismissBoxValue.EndToStart -> {
                                onDelete(stickNote)
                            }

                            SwipeToDismissBoxValue.Settled -> {}
                        }

                        MySwippe(
                            onUpdateStateNotificaion = onUpdateStateNotificaion,
                            stickNote = stickNote,
                            context = context,
                            dismissState = switToDismessState
                        )
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Você Ainda não adcionou nenhum lembrete",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)

                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ElevatedButton(onClick = {
                        val intent =
                            Intent(context.applicationContext, AddStickNoteActivity::class.java)

                        context.startActivity(intent)
                    }) {
                        Text(
                            text = "Cria novo Lembrete",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

    }


}





@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySwippe(
    modifier : Modifier = Modifier,
    onUpdateStateNotificaion : (StickyNoteDomain)->Unit,
    dismissState: SwipeToDismissBoxState,
    stickNote :StickyNoteDomain,
    context :Context,
){

    var color  =Color.Transparent
    var align  =Arrangement.End
    var icon = R.drawable.delete_24
    var textTitle = ""

    when(dismissState.dismissDirection ){
        SwipeToDismissBoxValue.EndToStart->{
            color =Color.Red
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
        MyCardView(stickNote, onUpdateStateNotificaion = onUpdateStateNotificaion , modifier = modifier , context = context)
    }
}

@Composable
fun BoxSwipToDismis(
    contentAlignment: Arrangement.Horizontal,
    backGroundcolor :Color,
    icon : Int ,
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




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ObserveModel(viewModel :StickNoteViewmodel , context :Context){
      val state by viewModel.stickNoteState.collectAsState()
    when(state) {
        is StickNoteState.Success -> {
            MyApp(
                stickyNoteList =  (state as StickNoteState.Success).stickNoteList,
               onUpdateStateNotificaion =  viewModel::updateStiickNote,
                onDelete = viewModel::deleteStickNote,
                context= context
            )
        }

        is StickNoteState.Error -> {
            Toast.makeText(context, (state as StickNoteState.Error).message, Toast.LENGTH_SHORT).show()
            Log.i("INFO_", "onCreate: Erroo")
        }

        StickNoteState.Loading -> {
            CircularProgressIndicator()
        }
    
    }
      
}




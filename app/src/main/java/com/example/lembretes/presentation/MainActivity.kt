package com.example.lembretes.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lembretes.presentation.navigation.AddStickNoteNavigation
import com.example.lembretes.presentation.navigation.HomeNavigation
import com.example.lembretes.presentation.ui.addsticknote.AddStickNoteScreen
import com.example.lembretes.presentation.ui.home.HomeScreen
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.AddUpdateViewModel
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private  val viewModel by  viewModels<StickNoteViewmodel>()
    private  val addUpdateViewModel by  viewModels<AddUpdateViewModel>()
    private  val userViewModel by  viewModels<UserViewModel>()

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
                        MyApp(
                            modifier = Modifier,
                            context = this,
                            viewModel = viewModel,
                            userViewModel = userViewModel,
                            addUpdateViewModel = addUpdateViewModel
                        )
                }
            }
        }
    }
    override fun onStart() {
        userViewModel.findFirstUser()
        super.onStart()
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(
     modifier: Modifier = Modifier ,
     context: Context,
     viewModel: StickNoteViewmodel,
     userViewModel :UserViewModel,
     addUpdateViewModel: AddUpdateViewModel
) {
     val navController = rememberNavController()

    NavHost(
        navController =navController ,
        startDestination = HomeNavigation.route,
    ) {

        composable(route = HomeNavigation.route){
            viewModel.alterFilterType(viewModel.uiState.value.filterType)
                HomeScreen(
                    userViewModel = userViewModel,
                    modifier = modifier,
                    context = context,
                    viewModel = viewModel,
                    onUpdateStateNotificaion = viewModel::updateNotificatioStickNote,
                    onDelete = viewModel::deleteStickNote,
                    onNavigateToAddStickNote = {
                        navController.navigate(AddStickNoteNavigation.route)
                    }
               )
        }
        composable(route = AddStickNoteNavigation.route){
            AddStickNoteScreen(
                modifier = modifier,
                stickNoteViewmodel = addUpdateViewModel,
                onClosed ={

                    navController.popBackStack()
                          },
                reloadoList = {

                }
            )
        }

    }
}













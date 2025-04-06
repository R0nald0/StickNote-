package com.example.lembretes.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lembretes.presentation.navigation.AddStickNoteNavigation
import com.example.lembretes.presentation.navigation.HomeNavigation
import com.example.lembretes.presentation.navigation.SearchNavigation
import com.example.lembretes.presentation.navigation.SettingNavigation
import com.example.lembretes.presentation.navigation.navigateToAddStiCkNote
import com.example.lembretes.presentation.ui.SearchScreen
import com.example.lembretes.presentation.ui.addsticknote.AddStickNoteScreen
import com.example.lembretes.presentation.ui.home.HomeScreen
import com.example.lembretes.presentation.ui.settings.SettingScreen
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.PreferencesViewModel
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    private  val prefViewModel by  viewModels<PreferencesViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
         splashScreen.setKeepOnScreenCondition{
             false
         }
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userPef  by  prefViewModel.userPreference.collectAsStateWithLifecycle()

            LembretesTheme(
                darkTheme = if (userPef.isDarkMode == 1 ) false else if(userPef.isDarkMode == 2)  true else isSystemInDarkTheme()
            )   {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NavHost(
                        navController =navController ,
                        startDestination = HomeNavigation.route,
                    ) {
                        composable(route = HomeNavigation.route){
                          val stickNoteViewModel  = hiltViewModel<StickNoteViewmodel>()
                            LaunchedEffect( Unit) {
                                stickNoteViewModel.alterFilterType(stickNoteViewModel.uiState.value.filterType)
                            }

                            HomeScreen(
                                modifier = Modifier,
                                stickNoteViewModel = stickNoteViewModel,
                                context = this@MainActivity,
                                onUpdate = {stickyNote ->
                                 val stickNoteJson   = Gson().toJson(stickyNote)
                                    navController.navigateToAddStiCkNote(stickNoteJson)
                                },
                                onNavigateToAddStickNote = {
                                    navController.navigate(AddStickNoteNavigation.route)
                                },
                                onNavigateToSettingsScreen = {
                                    navController.navigate(SettingNavigation.route)
                                },
                                openSearch = {navController.navigate(SearchNavigation.route)}
                            )
                        }
                        composable(
                            route = AddStickNoteNavigation.routeWithArgs,
                            arguments = AddStickNoteNavigation.arguments,
                        ){ backStackEntry->
                            val stickNoteJson = backStackEntry.arguments?.getString(AddStickNoteNavigation.idStickNote)

                            Log.i("INFO_", "onCreate: $stickNoteJson")
                            AddStickNoteScreen(
                                stickNoteJson = stickNoteJson,
                                modifier = Modifier,
                                activity = this@MainActivity,
                                onClosed = navController::popBackStack,
                            )
                        }

                        composable(route = SettingNavigation.route){
                            SettingScreen(
                                preferencesViewModel = prefViewModel,
                                modifier = Modifier,
                                onClosed = {navController.popBackStack()}
                            )
                        }
                        composable(route = SearchNavigation.route){
                            val  stickNoteViewModel = hiltViewModel<StickNoteViewmodel>()
                            SearchScreen(
                                modifier = Modifier,
                                onClose = {navController.popBackStack()},
                                context= this@MainActivity,
                                onUpadteNotification = {stickNote->
                                    if (stickNote?.id == null) return@SearchScreen
                                       stickNoteViewModel.updateNotificatioStickNote(stickNote){

                                    }
                                },
                            )
                        }

                    }
                }

            }
        }
    }


}














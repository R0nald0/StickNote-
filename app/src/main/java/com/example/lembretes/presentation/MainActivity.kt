package com.example.lembretes.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                darkTheme = userPef.isDakrMode
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
                                    navController.navigateToAddStiCkNote(stickyNote.id.toString())
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
                            AddStickNoteScreen(
                                idStikcNote = backStackEntry.arguments?.getString(AddStickNoteNavigation.idStickNote),
                                modifier = Modifier,
                                onClosed = navController::popBackStack,
                            )
                        }

                        composable(route = SettingNavigation.route){
                            SettingScreen(
                                modifier = Modifier,
                                userPreference = userPef,
                                preferencesViewModel = prefViewModel,
                                onClosed = {navController.popBackStack()}
                            )
                        }
                        composable(route = SearchNavigation.route){
                            val  stickNoteViewModel = hiltViewModel<StickNoteViewmodel>()
                            SearchScreen(
                                modifier = Modifier,
                                onClose = {navController.popBackStack()},
                                context= this@MainActivity,
                                onUpadteNotification = stickNoteViewModel::updateNotificatioStickNote,
                            )
                        }

                    }
                }

            }
        }
    }

}














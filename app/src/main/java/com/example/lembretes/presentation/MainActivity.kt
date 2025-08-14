package com.example.lembretes.presentation

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lembretes.core.notification.SnackBarEnumType
import com.example.lembretes.core.notification.StickNoteSnackBar
import com.example.lembretes.presentation.navigation.AddStickNoteNavigation
import com.example.lembretes.presentation.navigation.HomeNavigation
import com.example.lembretes.presentation.navigation.SearchNavigation
import com.example.lembretes.presentation.navigation.SettingNavigation
import com.example.lembretes.presentation.navigation.navigateToAddStiCkNote
import com.example.lembretes.presentation.ui.SearchScreen
import com.example.lembretes.presentation.ui.addsticknote.AddStickNoteScreen
import com.example.lembretes.presentation.ui.home.HomeScreen
import com.example.lembretes.presentation.ui.settings.SettingScreen
import com.example.lembretes.presentation.ui.theme.GlobalSizeFont
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.GlobalState
import com.example.lembretes.presentation.viewmodel.GlobalVIewModel
import com.example.lembretes.presentation.viewmodel.PreferencesViewModel
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


data class StickNoteNavItem(
    val index: String,
    val icon: ImageVector,
    val title: String,
    val enabled: Boolean,
    val selected: Boolean,
    val onClick: () -> Unit,
    )

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val prefViewModel by viewModels<PreferencesViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val globalViewModel by viewModels<GlobalVIewModel>()

    val navMenuItems = listOf(
        StickNoteNavItem(
            index = HomeNavigation.route,
            title = "Home",
            enabled = true,
            icon = Icons.Filled.Home,
            selected = true,
            onClick = {}),
        StickNoteNavItem(
            index = AddStickNoteNavigation.route,
            title = "Adicionar",
            enabled = true,
            icon = Icons.Outlined.AddCircle,
            selected = false,
            onClick = {}),
        StickNoteNavItem(
            index = SettingNavigation.route,
            title = "Configurações",
            enabled = true,
            icon = Icons.Default.Settings,
            selected = false,
            onClick = {}),
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
              val(_,sizeTitleStickNote,sizeDescriptionStickNote,loading) = prefViewModel.userPreference.value
                GlobalSizeFont.titleSize = sizeTitleStickNote ?: 20
                GlobalSizeFont.descriptionSize = sizeDescriptionStickNote ?: 14
            loading
        }

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController( )
            val userPef by prefViewModel.userPreference.collectAsStateWithLifecycle()
            val globalState by globalViewModel.globalState.collectAsStateWithLifecycle(GlobalState())

            LembretesTheme(
                darkTheme =  if (userPef.isDarkMode == 1) false else if (userPef.isDarkMode == 2) true else isSystemInDarkTheme()
            ) {
                var selectedPage by remember {
                    mutableStateOf(HomeNavigation.route)
                }

                var snackBarEnumType by remember { mutableStateOf(SnackBarEnumType.INFO) }
                val snackBarHots by remember { mutableStateOf(SnackbarHostState())  }
                val scope = rememberCoroutineScope()
                val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
                val route = currentBackStackEntryAsState?.destination?.route
                val goToHomeRoute = route == HomeNavigation.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        StickNoteSnackBar( snackbarHostState  =snackBarHots,modifier = Modifier, snackBarEnumType = snackBarEnumType)
                    },

                    bottomBar = {
                        BottomNavigation(
                            modifier = Modifier.navigationBarsPadding(),
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            navMenuItems.forEach { item ->
                                BottomNavigationItem(
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = {
                                        Text(
                                            item.title,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    enabled = item.enabled,
                                    alwaysShowLabel = true,
                                    onClick = {
                                        navigatoToAndClearBackStack(navController, item.index)
                                        selectedPage = item.index
                                    },
                                    selected = item.index == selectedPage,
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        navController = navController,
                        startDestination = HomeNavigation.route,
                    ) {
                        composable(
                            route = HomeNavigation.route
                        ) {
                            val stickNoteViewModel = hiltViewModel<StickNoteViewmodel>()
                            LaunchedEffect(Unit) {
                                stickNoteViewModel.alterFilterType(stickNoteViewModel.uiState.value.filterType)
                            }

                            HomeScreen(
                                globalVIewModel = globalViewModel,
                                modifier = Modifier,
                                prefViewModel = prefViewModel,
                                stickNoteViewModel = stickNoteViewModel,
                                userViewModel = userViewModel,
                                context = this@MainActivity,
                                onUpdate = { stickyNote ->
                                    val stickNoteJson = Gson().toJson(stickyNote)
                                    navController.navigateToAddStiCkNote(stickNoteJson)
                                },
                                onNavigateToAddStickNote = {
                                    selectedPage = navigatoToAndClearBackStack(
                                        navController,
                                        AddStickNoteNavigation.route
                                    )
                                },
                                openSearch = { navController.navigate(SearchNavigation.route) },
                                onErrorMessage = { error ->
                                    scope.launch {
                                        snackBarEnumType = SnackBarEnumType.ERROR
                                        snackBarHots.showSnackbar(
                                            message = error
                                        )
                                    }
                                },
                                onInfoMessage = {infoMessage->
                                    scope.launch {
                                        snackBarEnumType = SnackBarEnumType.INFO
                                        snackBarHots.showSnackbar(
                                            message = infoMessage
                                        )
                                    }
                                }
                            )
                        }
                        composable(
                            route = AddStickNoteNavigation.routeWithArgs,
                            arguments = AddStickNoteNavigation.arguments,
                        ) { backStackEntry ->
                            val stickNoteJson =
                                backStackEntry.arguments?.getString(AddStickNoteNavigation.idStickNote)
                            AddStickNoteScreen(
                               globalVIewModel = globalViewModel,
                                stickNoteJson = stickNoteJson,
                                modifier = Modifier,
                                activity = this@MainActivity,
                                onClosed = {
                                  selectedPage = navigatoToAndClearBackStack(navController, HomeNavigation.route)
                                },
                                onInfo = {infoMessage->
                                    scope.launch {
                                        snackBarEnumType = SnackBarEnumType.INFO
                                        snackBarHots.showSnackbar(
                                            message = infoMessage
                                        )
                                    }
                                },
                                onError = { error->
                                    scope.launch {
                                        snackBarEnumType = SnackBarEnumType.ERROR
                                        snackBarHots.showSnackbar(
                                            message = error
                                        )
                                    }
                                }
                            )
                            BackHandler(enabled = goToHomeRoute.not()) {
                                selectedPage =
                                    navigatoToAndClearBackStack(navController, HomeNavigation.route)
                            }
                        }
                        composable(route = SettingNavigation.route) {
                            SettingScreen(
                                globalVIewModel = globalViewModel,
                                preferencesViewModel = prefViewModel,
                                userPreference = userPef,
                                modifier = Modifier,
                            )
                            BackHandler(enabled = goToHomeRoute.not()) {
                                selectedPage =
                                    navigatoToAndClearBackStack(navController, HomeNavigation.route)
                            }
                        }
                        composable(route = SearchNavigation.route) {
                           val focus=  LocalFocusManager.current
                            val stickNoteViewModel = hiltViewModel<StickNoteViewmodel>()
                            SearchScreen(
                                modifier = Modifier,
                                onClose = { navController.popBackStack() },
                                context = this@MainActivity,
                                onUpadteNotification = {stickNote ,isRemember ->
                                    focus.clearFocus()
                                    if (stickNote?.id == null) return@SearchScreen
                                    stickNoteViewModel.updateNotificatioStickNote(
                                        stickNote.copy(isRemember = isRemember)) {errorMessage ->
                                           if (errorMessage == null) return@updateNotificatioStickNote
                                         Toast.makeText(this@MainActivity, "Notificação de lembrete alterado", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onInfoMessage = {infoMessage->
                                    focus.clearFocus()
                                    scope.launch {
                                        snackBarEnumType = SnackBarEnumType.INFO
                                        snackBarHots.showSnackbar(
                                            message = infoMessage
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
                if (globalState.isLoading){
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color = Color.Gray.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center,
                        content = {
                            CircularProgressIndicator()
                        }
                    )
                }
            }

        }
    }
    private fun navigatoToAndClearBackStack(
        navController: NavHostController,
        route : String
    ): String {
        navController.navigate(route) {
            popUpTo(route) {
                inclusive = true
            }
            launchSingleTop = true
        }
        return route
    }


}
















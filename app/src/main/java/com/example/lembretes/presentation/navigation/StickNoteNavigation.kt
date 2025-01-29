package com.example.lembretes.presentation.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface StickNoteNavigation {
    var route :String
}

object HomeNavigation :StickNoteNavigation {
    override var route: String = "home"
}
object AddStickNoteNavigation :StickNoteNavigation{
    override var route: String ="add"
    const val idStickNote = "idStickNote"
    var routeWithArgs: String ="$route?idStickNote={${idStickNote}}"
    var arguments = listOf(
        navArgument(
           idStickNote,
        ){
            nullable = true
            defaultValue="0"
            type = NavType.StringType
        }
    )
}
object SettingNavigation :StickNoteNavigation{
    override var route: String ="settings"
}

object SearchNavigation :StickNoteNavigation{
    override var route: String ="search"
}



fun NavHostController.navigateToAddStiCkNote(idStickNote : String?){
     this.navigate("${AddStickNoteNavigation.route}?${AddStickNoteNavigation.idStickNote}=$idStickNote")
}

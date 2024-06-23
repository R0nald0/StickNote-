package com.example.lembretes.presentation.navigation

interface StickNoteNavigation {
    var route :String
}

object HomeNavigation :StickNoteNavigation {
    override var route: String = "/home"
}
object AddStickNoteNavigation :StickNoteNavigation{
    override var route: String ="/add/sticknote"
}

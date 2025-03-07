package com.example.lembretes.core

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    const val PREFERENCE_NAME ="preferences"
   const  val CHANNEL_ID = "stick_note_notify"
     const val NOTIFICATION_NAME = "Lembrete"
     const val NOTIFICATION_INTENT_REQUEST_CODE = 0
    val ID_KEY_UI_MODE = booleanPreferencesKey("ui_mode_key")
}
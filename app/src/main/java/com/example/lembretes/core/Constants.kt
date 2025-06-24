package com.example.lembretes.core

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.datetime.TimeZone

object Constants {
    val STICK_NOTE_TIME_ZONE = TimeZone.currentSystemDefault()
     const val PREFERENCE_NAME ="preferences"
     const  val CHANNEL_ID = "stick_note_notify"
     const val NOTIFICATION_NAME = "Lembrete"
     const val NOTIFICATION_INTENT_REQUEST_CODE = 0

     val ZONE_TIME_KEY_PREFERENCES = stringPreferencesKey("zone_time_pref")
     val SIZE_TITLE_STICKNOTE = intPreferencesKey("size_title_stick_note")
     val SIZE_DESCRIPTION_STICKNOTE = intPreferencesKey("size_description_stick_note")
     val ID_KEY_UI_MODE = intPreferencesKey("ui_mode_key")
     val USER_KEY = stringPreferencesKey("user_data_preference")
}
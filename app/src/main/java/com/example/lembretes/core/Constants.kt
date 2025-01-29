package com.example.lembretes.core

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    const val PREFERENCE_NAME ="preferences"
    val ID_KEY_UI_MODE = booleanPreferencesKey("ui_mode_key")
}
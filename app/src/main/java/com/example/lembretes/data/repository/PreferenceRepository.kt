package com.example.lembretes.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.lembretes.core.Constants
import com.example.lembretes.core.exception.PreferencesException
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.presentation.viewmodel.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import javax.inject.Inject


class PreferenceRepository @Inject constructor(
    private  val dataStore : DataStore<Preferences>
) {
      fun <T> readKey(key : Preferences.Key<T>): Flow<Preferences> {
       try {
          return  dataStore.data
       }catch (io :IOException){
           Log.i("INFO_", "readKey: ${io.message} : ${io.stackTrace}")
           throw PreferencesException(
               message = "erro ao ler preferencias",
               cause = io
           )
       }

    }
      fun readAllPreference(): Flow<UserPreference> {
        try {
              return  dataStore.data.map {pref ->
                  UserPreference(
                      isDarkMode = pref[Constants.ID_KEY_UI_MODE] ?: 3,
                      timeZone = pref[Constants.ZONE_TIME_KEY_PREFERENCES] ?: TimeZone.currentSystemDefault().toString(),
                      sizeTitleStickNote = pref[Constants.SIZE_TITLE_STICKNOTE] ?: 16
                  )
              }

        }catch (io :IOException){
            StickNoteLog.error("readKey: ${io.message}",io)
            throw PreferencesException(
                message = "erro ao ler preferencias",
                cause = io
            )
        }

    }
    suspend fun <T> savePreference(value : T,key: Preferences.Key<T>): Preferences?{
        try {
          val preferences  = dataStore.edit {
                it[key] = value
            }
           return preferences
        }catch (io: IOException){
            StickNoteLog.error("savePreference saveValue: ${io.message} : ${io.stackTrace}",io)
            throw PreferencesException(
                message = "Erro ao salvar preferencia $value",
                cause = io
            )
        }
    }
}
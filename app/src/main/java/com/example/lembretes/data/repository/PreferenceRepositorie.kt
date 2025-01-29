package com.example.lembretes.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.lembretes.core.Constants
import com.example.lembretes.core.excetion.PreferencesException
import com.example.lembretes.presentation.viewmodel.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferenceRepositorie @Inject constructor(
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
                      isDakrMode = pref[Constants.ID_KEY_UI_MODE] ?: false
                  )
              }

        }catch (io :IOException){
            Log.i("INFO_", "readKey: ${io.message} : ${io.stackTrace}")
            throw PreferencesException(
                message = "erro ao ler preferencias",
                cause = io
            )
        }

    }
    suspend fun <T> savePreference(value : T,key: Preferences.Key<T>){
        try {
            dataStore.edit {
                it[key] = value
            }
        }catch (io: IOException){
            Log.i("INFO_", "saveValue: ${io.message} : ${io.stackTrace}")
            throw PreferencesException(
                message = "erro ao salvar preferencia",
                cause = io
            )
        }
    }



}
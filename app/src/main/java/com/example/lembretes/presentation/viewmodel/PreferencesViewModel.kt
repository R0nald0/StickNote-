package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.core.Constants
import com.example.lembretes.data.repository.PreferenceRepositorie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserPreference(
    var isDarkMode :Int?=null,
    var loading : Boolean =false
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferenceRepositorie
) :ViewModel(){

    private val _userPreference = MutableStateFlow(UserPreference())
    var userPreference : StateFlow<UserPreference> = _userPreference.asStateFlow()

    init {
        readAllPreferences()
    }

    fun readUniquePreference(){
        _userPreference.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
             preferencesRepository.readKey(Constants.ID_KEY_UI_MODE).collect{ preference ->
                 _userPreference.update {
                     it.copy(
                         isDarkMode = preference[Constants.ID_KEY_UI_MODE] ?: 3,
                         loading = false
                         )
                 }
             }
        }
    }
    fun readAllPreferences(){
        _userPreference.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            delay(2000)
            preferencesRepository.readAllPreference().collect{ preference->
                 _userPreference.update {userPref->
                     userPref.copy(isDarkMode = preference.isDarkMode, loading = false)
                 }
                Log.i("INFO_)", "readAllPreferences: ${_userPreference.value}")
            }
        }
    }
    fun updateDarkMode(isDarkMode :Int){
        viewModelScope.launch {
           runCatching {
               preferencesRepository.savePreference(isDarkMode,Constants.ID_KEY_UI_MODE)
           }.fold(
               onSuccess = {
                   Log.i("INFO_", "updateDarkMode:  Mode ui atualizado com sucesso")
               },
               onFailure = {erro->
                   Log.i("INFO_", "updateDarkMode:  erro ao atualizar DarkMode")
                   Log.e("erro","${erro.message} , ${erro.stackTrace} ",erro)
               }
           )
        }
    }

}
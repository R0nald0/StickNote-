package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.core.Constants
import com.example.lembretes.data.repository.PreferenceRepositorie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserPreference(
    var isDakrMode :Boolean = false,
){
    constructor():this(
        isDakrMode = false
    )
}
@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepositorie: PreferenceRepositorie
) :ViewModel(){

    private val _isDarkMode = MutableStateFlow(false)
    var isDarkMode : StateFlow<Boolean> = _isDarkMode.asStateFlow()


    private val _userPreference = MutableStateFlow(UserPreference())
    var userPreference : StateFlow<UserPreference> = _userPreference.asStateFlow()
    fun readUniquePreference(){
        viewModelScope.launch {
             preferencesRepositorie.readKey(Constants.ID_KEY_UI_MODE).collect{
                 _isDarkMode.value = it[Constants.ID_KEY_UI_MODE] ?: false
             }
        }
    }

    init {
        readAllPreferences()
    }
    fun readAllPreferences(){
        viewModelScope.launch {
            preferencesRepositorie.readAllPreference().collect{preference->
                 _userPreference.update {
                     it.copy(isDakrMode = preference.isDakrMode)
                 }
            }
        }
    }
    fun updateDarkMode(isDarkMode :Boolean){
        viewModelScope.launch {
           runCatching {
               preferencesRepositorie.savePreference(isDarkMode,Constants.ID_KEY_UI_MODE)
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
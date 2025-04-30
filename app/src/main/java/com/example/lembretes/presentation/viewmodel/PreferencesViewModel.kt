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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserPreference(
    var isDarkMode :Int?=null,
    val loading: Boolean = false,
    val erroMessage : String? = null
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
        viewModelScope.launch {
             preferencesRepository.readKey(Constants.ID_KEY_UI_MODE)
                 .catch { error->

                 }
                 .collect{ preference ->
                 _userPreference.update {
                     it.copy(
                         isDarkMode = preference[Constants.ID_KEY_UI_MODE] ?: 3,)
                 }
             }
        }
    }
    fun readAllPreferences(){
        _userPreference.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            preferencesRepository.readAllPreference()
                .catch {error->
                    Log.e("Error", "readAllPreferences: Erro",error )
                    _userPreference.update {
                        it.copy(loading = false, erroMessage = "Não conseguimos buscar as preferências")
                    }
                }
                .collect{ preference->
                 _userPreference.update {userPref->
                     userPref.copy(isDarkMode = preference.isDarkMode,loading = false)
                 }
            }
        }
    }
    fun updateDarkMode(isDarkMode :Int){
        _userPreference.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
           runCatching {
               preferencesRepository.savePreference(isDarkMode,Constants.ID_KEY_UI_MODE)
           }.fold(
               onSuccess = {
                   _userPreference.update {
                       it.copy(loading = false)
                   }
                   Log.i("INFO_", "updateDarkMode:  Mode ui atualizado com sucesso")
               },
               onFailure = {erro->
                   _userPreference.update {
                       it.copy(loading = false, erroMessage = "Erro ao atualizar preferência")
                   }
                   Log.e("erro","${erro.message} , ${erro.stackTrace} ",erro)
               }
           )
        }
    }

}
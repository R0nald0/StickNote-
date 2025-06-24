package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.core.Constants
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserPreference(
    val isDarkMode :Int?=null,
    val sizeTitleStickNote : Int? =null ,
    val sizeDescriptionStickNote : Int? =null ,
    val loading: Boolean = false,
    val errorMessage : String? = null
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferenceRepository
) :ViewModel(){

    private val _userPreference = MutableStateFlow(UserPreference())
    var userPreference : StateFlow<UserPreference> = _userPreference.asStateFlow()

    init {
        readAllPreferences()
    }

    fun readUniquePreference(){
        viewModelScope.launch {
             preferencesRepository.readKey(Constants.ID_KEY_UI_MODE)
                 .catch { error-> }
                 .collect{ preference ->
                 _userPreference.update {
                     it.copy(
                         isDarkMode = preference[Constants.ID_KEY_UI_MODE] ?: 3,)
                 }
             }
        }
    }
    fun readAllPreferences(){
        _userPreference.update { it.copy(loading = true) }
        viewModelScope.launch {
            preferencesRepository.readAllPreference()
                .catch {
                    error->
                    StickNoteLog.error("Erro ao buscar Preferências",error)
                    _userPreference.update {
                        it.copy(loading = false, errorMessage = "Não conseguimos buscar as preferências")
                    }
                }
                .collect{
                    preference->
                    _userPreference.update {userPref->
                        StickNoteLog.info("State atualizado $preference")
                        userPref.copy(
                            isDarkMode = preference.isDarkMode,
                            loading =false,
                            sizeTitleStickNote = preference.sizeTitleStickNote,
                            sizeDescriptionStickNote = preference.sizeDescriptionStickNote
                            )
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
                   StickNoteLog.info("updateDarkMode:  Mode ui atualizado com sucesso")
               },
               onFailure = {
                   error->
                   _userPreference.update {
                       it.copy(loading = false, errorMessage = "Erro ao atualizar preferência")
                   }
                   StickNoteLog.error("Error ao atualizar Preferências",error)
               }
           )
        }
    }
    fun  updateSizeTitle(size : Int){
        _userPreference.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
             runCatching {
                 preferencesRepository.savePreference(size, Constants.SIZE_TITLE_STICKNOTE)
             }.fold(
                 onSuccess = { pref ->
                     if (pref == null) return@launch
                     _userPreference.update{it.copy(
                         loading = false,
                         sizeTitleStickNote = pref[Constants.SIZE_TITLE_STICKNOTE]!!
                     ) }
                 },
                 onFailure = {error->
                     Log.e("ERROR", "update item: ${error.message} ",error )
                     _userPreference.update {
                         it.copy(loading = false, errorMessage = error.message )
                     }
                 }
             )
        }
    }
    fun  updateDescription(size : Int){
        _userPreference.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            runCatching {
                preferencesRepository.savePreference(size, Constants.SIZE_DESCRIPTION_STICKNOTE)
            }.fold(
                onSuccess = { pref ->
                    if (pref == null) return@launch
                    _userPreference.update{it.copy(
                        loading = false,
                        sizeDescriptionStickNote = pref[Constants.SIZE_DESCRIPTION_STICKNOTE] ?: 11
                    ) }
                },
                onFailure = {error->
                    Log.e("ERROR", "update item: ${error.message} ",error )
                    _userPreference.update {
                        it.copy(loading = false, errorMessage = error.message )
                    }
                }
            )
        }
    }

}
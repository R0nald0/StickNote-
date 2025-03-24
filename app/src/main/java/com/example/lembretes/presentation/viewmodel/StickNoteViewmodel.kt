package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.User
import com.example.lembretes.domain.usecase.sticknote.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import com.example.lembretes.domain.usecase.user.FindUser
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val listData :List<StickyNoteDomain>? =null,
    var stickNoteToRemember :List<StickyNoteDomain> = emptyList(),
    val user : User = User(),
    val filterType: StickNoteEnumFilterType =StickNoteEnumFilterType.Today,
    val scheduledReminders : Int= 0,
    val isLoading : Boolean = false,
    val erro :String? =null,
)

@HiltViewModel
class StickNoteViewmodel @Inject constructor(
    private val getStickyNoteUseCaseImpl: GetStickyNoteUseCase,
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val deleteStickNoteUseCase: DeleteStickNoteUseCase,
    private val findUser: FindUser,
    private val validateStickNoteUseCase: ValidateStickNoteUseCase
    ):ViewModel() {

    val TAG = "_INFO"
    private val _uiState = MutableStateFlow(HomeState())
    var uiState :StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        findFirstUser()
        alterFilterType(uiState.value.filterType)
        findStickNoteToRemeber()
    }

    fun findStickNoteToRemeber(){
        viewModelScope.launch {
            val allNotes =getStickyNoteUseCaseImpl.getStickyNotes().map {
                it.filter { stickNote->
                    stickNote.isRemember
                }
            }
            allNotes.collect{
                uiState.value.stickNoteToRemember = it
                it.forEach {stickNote ->
                   val isValidDate  = validateStickNoteUseCase.validateUpdateNotifcation(stickNote.dateTime)
                    if (!isValidDate){
                        updateStickNoteUseCase.updateNotificatioStickNote(stickNote.id!!,false)
                    }
                    Log.i(TAG, "findStickNoteToRemeber:Title ${stickNote.name} - remember: ${stickNote.isRemember}")
                }
            }
        }
    }

     fun alterFilterType(typeFilter : StickNoteEnumFilterType){
           viewModelScope.launch {
               _uiState.update {
                  it.copy(filterType = typeFilter)
               }

               when(_uiState.value.filterType){
                  StickNoteEnumFilterType.Today ->{
                       val todayNotes = getStickyNoteUseCaseImpl.getStickNotesToday()
                       getStickNotes(todayNotes)
                   }
                   StickNoteEnumFilterType.TOMORROW->{
                       val tomorrowNotes =getStickyNoteUseCaseImpl.getStickNotesTomorrow()
                       getStickNotes(tomorrowNotes)
                   }
                   StickNoteEnumFilterType.All ->{
                       val allNotes =getStickyNoteUseCaseImpl.getStickyNotes()
                       getStickNotes(allNotes)
                   }
               }
           }
     }

    private  fun getStickNotes(onFind: Flow<List<StickyNoteDomain>>){
   viewModelScope.launch {

       _uiState.update {
           it.copy(isLoading = true, listData = null)
       }
       onFind
            .catch { error ->
                _uiState.update {
                    it.copy(
                        erro = error.message ?: "Erro ao buscar os lembretes"
                    )
                }
            }
           .collect { stickNotes ->
               val scheduledReminders = stickNotes.filter {
                    it.isRemember
               }.size
               _uiState.update {
                it.copy(
                    listData = stickNotes,
                    scheduledReminders = scheduledReminders,
                    filterType = _uiState.value.filterType,
                    isLoading = false
                )
               }
           }
     }
  }
    fun updateNotificatioStickNote(stickyNoteDomain: StickyNoteDomain,createAlarm:(String?)-> Unit){
        viewModelScope.launch {
            _uiState.update { ui->
                ui.copy(isLoading = true)
            }
         runCatching {
             val isValidDate= validateStickNoteUseCase.validateUpdateNotifcation(stickyNoteDomain.dateTime)
            if (!isValidDate) {
               createAlarm("Data Inavalida Para Atualizar")
                _uiState.update { ui->
                    ui.copy(isLoading = false)
                }
                return@launch
            }

             updateStickNoteUseCase.updateNotificatioStickNote(stickyNoteDomain.id!!,stickyNoteDomain.isRemember)

         }.fold(
             onSuccess = {
                  alterFilterType(_uiState.value.filterType)
                 _uiState.update { ui->
                     ui.copy(isLoading = false)
                 }
                 createAlarm(null)
             },
             onFailure = {erro->
                 _uiState.update { ui->
                     ui.copy(isLoading = false)
                 }
                 Log.i("_INFO", "update:erro ao atualizar lembrete : ${erro.message}")
             }
         )
        }
    }
    fun deleteStickNote(stickyNoteDomain: StickyNoteDomain,onCallCaback:()-> Unit){
        viewModelScope.launch {
             runCatching {
                 deleteStickNoteUseCase.deleteStickNote(stickyNoteDomain)
             }.fold(
                 onSuccess = {
                     if (it > 0) alterFilterType(_uiState.value.filterType)
                     onCallCaback()
                 },

                 onFailure = {erro->
                     Log.i(TAG, "deleteStickNote: erro ao deletetar ${erro.message }")
                 },
             )
        }
    }

    fun findFirstUser(){
        viewModelScope.launch {
           kotlin.runCatching {
                findUser.findFistUser().first()
           } .fold(
               onSuccess = {user ->
                   _uiState.update {
                       it.copy(user = user)
                   }
               },
               onFailure ={erro ->
                   Log.i(TAG, "deleteStickNote: erro ao deletetar ${erro.message }")
               },
           )
        }
    }


}

package com.example.lembretes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.sticknote.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val listData :List<StickyNoteDomain>? =null,
    val isLoading : Boolean = false,
    var stickNoteToRemember :List<StickyNoteDomain> = emptyList(),
    val filterType: StickNoteEnumFilterType =StickNoteEnumFilterType.Today,
    val scheduledReminders : Int= 0,
    val error :String? =null,
)

@HiltViewModel
class StickNoteViewmodel @Inject constructor(
    private val getStickyNoteUseCaseImpl: GetStickyNoteUseCase,
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val deleteStickNoteUseCase: DeleteStickNoteUseCase,
    private val validateStickNoteUseCase: ValidateStickNoteUseCase,
    ): ViewModel(){

    private val _uiState = MutableStateFlow(HomeState())
    var uiState :StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        alterFilterType(uiState.value.filterType)
        findStickNoteToRemember()
    }

    fun findStickNoteToRemember(){
        viewModelScope.launch {
            val allNotes =getStickyNoteUseCaseImpl.getStickyNotes().map {
                it.filter { stickNote->
                    stickNote.isRemember
                }
            }
            allNotes
                .catch { erro ->
                    StickNoteLog.error(
                        "findStickNoteToRemember:Title ${erro.message}",
                        erro)
                    _uiState.update {
                        it.copy(error = "Erro ao buscar lembretes ",isLoading = false)
                    }
                }
                .collect{
                uiState.value.stickNoteToRemember = it
                it.forEach {stickNote ->
                   val isValidDate  = validateStickNoteUseCase.validateUpdateNotifcation(stickNote.dateTime)
                    if (!isValidDate){
                        updateStickNoteUseCase.updateNotificatioStickNote(stickNote.id!!,false)
                    }
                    StickNoteLog.info("findStickNoteToRemeber:Title ${stickNote.name} - remember: ${stickNote.isRemember}")
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
       clearErroMessage()
       onFind
            .catch { error ->
                _uiState.update {
                    it.copy(
                        error = error.message ?: "Erro ao buscar lembretes",isLoading = false
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
         runCatching {
             val isValidDate= validateStickNoteUseCase.validateUpdateNotifcation(stickyNoteDomain.dateTime)
            if (!isValidDate) {
                _uiState.update {
                    it.copy(error = "Data Inaválida para atualizar")
                }
                createAlarm("Data inválida para atualizar")
                return@launch
            }

             updateStickNoteUseCase.updateNotificatioStickNote(stickyNoteDomain.id!!,stickyNoteDomain.isRemember)
         }.fold(
             onSuccess = {
                  alterFilterType(_uiState.value.filterType)
                  StickNoteLog.info("STICKNOTE UP $stickyNoteDomain")
                  createAlarm(null)
             },
             onFailure = {error->
                  createAlarm("Erro ao atualizar Lembrete")
                 _uiState.update {
                     it.copy(error = "Erro ao atualizar Lembrete")
                 }
                 StickNoteLog.error("update:erro ao atualizar lembrete : ${error.message}",error)
             }
         )
        }
    }

     fun clearErroMessage() {
        _uiState.update { it.copy(error = null) }
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
                 onFailure = { error->
                     _uiState.update {
                         it.copy(error = "Não conseguimos deletar o lembrete")
                     }
                     StickNoteLog.error(
                         "deleteStickNote: erro ao deletetar ${error.message}",
                         error
                     )
                 },
             )
        }
    }

}

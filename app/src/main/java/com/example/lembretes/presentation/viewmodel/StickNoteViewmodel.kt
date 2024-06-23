package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.sticknote.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickNoteViewmodel @Inject constructor(
    private val getStickyNoteUseCaseImpl: GetStickyNoteUseCase,
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val deleteStickNoteUseCase: DeleteStickNoteUseCase,
    ):ViewModel() {
    val TAG = "_INFO"
    private val _uiState = MutableStateFlow(HomeState())
    var uiState :StateFlow<HomeState> = _uiState.asStateFlow()


     fun alterFilterType(typeFilter : StickNoteEnumFilterType){
           viewModelScope.launch {
               _uiState.update {
                  it.copy(filterType = typeFilter)
               }
               when(_uiState.value.filterType){
                  StickNoteEnumFilterType.Today ->{
                       val todayNotes =getStickyNoteUseCaseImpl.getStickNotesToday()
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

    fun updateNotificatioStickNote(idStickNote :Int,isRemember :Boolean){
        viewModelScope.launch {
         runCatching {
             updateStickNoteUseCase.updateNotificatioStickNote(idStickNote, isRemember)
         }.fold(
             onSuccess = {afectedKLine->
                 alterFilterType(_uiState.value.filterType)
             },
             onFailure = {erro->
                 Log.i("_INFO", "update:erro ao atualizar lembrete : ${erro.message}")
             }
         )
        }
    }
    fun deleteStickNote(stickyNoteDomain: StickyNoteDomain){
        viewModelScope.launch {
             runCatching {
                 deleteStickNoteUseCase.deleteStickNote(stickyNoteDomain)
             }.fold(
                 onSuccess = {
                     if (it > 0) alterFilterType(_uiState.value.filterType)
                 },
                 onFailure = {erro->
                     Log.i(TAG, "deleteStickNote: erro ao deletetar ${erro.message }")
                 },
             )
        }
    }


}

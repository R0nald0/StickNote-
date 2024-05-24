package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.impl.InsertStickNoteUseCase
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StickNoteViewmodel @Inject constructor(
    private val getStickyNoteUseCaseImpl: GetStickyNoteUseCase,
    private val insertStickNoteUseCase: InsertStickNoteUseCase,
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val deleteStickNoteUseCase: DeleteStickNoteUseCase,
):ViewModel() {
    val TAG = "_INFO"
    private val _stickNoteState  = MutableStateFlow<StickNoteState>(StickNoteState.Success(emptyList()))
    var stickNoteState : StateFlow<StickNoteState> = _stickNoteState.asStateFlow()
    private val _stickNoteEnumFilterType = MutableStateFlow(StickNoteEnumFilterType.Today)
    var stickNoteEnumFilterType = _stickNoteEnumFilterType.asStateFlow()
    private val _stickNotesAgendas = MutableStateFlow(0)
    var stickNotesAgendas =  _stickNotesAgendas.asStateFlow()

    init {
       alterFilterType(typeFilter = _stickNoteEnumFilterType.value)
    }

     fun alterFilterType(typeFilter : StickNoteEnumFilterType){

         _stickNoteEnumFilterType.value = typeFilter
           viewModelScope.launch {
               when(typeFilter){
                   StickNoteEnumFilterType.Today->{
                       getStickNotes(getStickyNoteUseCaseImpl.getStickNotesToday())
                   }
                   StickNoteEnumFilterType.TOMORROW->{
                       getStickNotes(getStickyNoteUseCaseImpl.getStickNotesTomorrow())
                   }
                   StickNoteEnumFilterType.All ->{
                       getStickNotes(getStickyNoteUseCaseImpl.getStickyNotes())
                   }
               }
           }
     }
 private suspend fun getStickNotes(onfind: Flow<List<StickyNoteDomain>>){
              _stickNoteState.value = StickNoteState.Loading
          val  stickNoteFlow = onfind
                 stickNoteFlow
                     .flowOn(Dispatchers.Main)
                     .catch {error->
                         _stickNoteState.value = StickNoteState.Error(message = error.message ?:"Erro ao buscar os lembretes")
                     }
                     .collect { stickNotes ->
                         _stickNoteState.value = StickNoteState.Success(stickNoteList = stickNotes)
                          _stickNotesAgendas.value = stickNotes.filter {stickyNote ->
                              stickyNote.isRemember
                          }.size
                     }

  }

    fun insertStickNote(stickyNoteDomain: StickyNoteDomain){
      viewModelScope.launch(Dispatchers.IO) {
          runCatching {
               insertStickNoteUseCase.insert(stickyNoteDomain)
          }.fold(
              onSuccess = {insertValue->
                  if (insertValue != 0L){

                      Log.i("INFO_", "insertStickNote: Inserido com sucesso")
                  }
              },
              onFailure = {erro->

                  Log.i("INFO_", "insertStickNote: Erro ao inserir ${erro}")
              }
          )
      }
  }
    fun updateStiickNote(stickyNoteDomain: StickyNoteDomain){
        viewModelScope.launch(Dispatchers.IO){
            runCatching {
                updateStickNoteUseCase.updateStickNote(stickNote = stickyNoteDomain)
            }.fold(
                onSuccess = {linesAfected->
                    Log.i(TAG, "updateStiickNote: ${linesAfected}")
                },
                onFailure = {error->
                    Log.i(TAG, "updateStiickNote: ${error}")
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
                 },
                 onFailure = {},
             )
        }
    }
}

sealed interface StickNoteState{

    data object Loading : StickNoteState
    class Success(val stickNoteList :List<StickyNoteDomain>) : StickNoteState
    class Error(val message:String) :StickNoteState

}
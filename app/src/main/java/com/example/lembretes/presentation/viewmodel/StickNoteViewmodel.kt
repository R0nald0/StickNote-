package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.impl.InsertStickNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    init {
       getStickNotes()
    }
     fun getStickNotes() {
          viewModelScope.launch {
             _stickNoteState.value =  StickNoteState.Loading
             getStickyNoteUseCaseImpl.getStickyNotes()
                 .flowOn(Dispatchers.Main)
                 .catch { erro ->
                     _stickNoteState.value = StickNoteState.Error("erro ao buscar os lembretes ${erro.message}")
                 }.collect { listStickNote ->
                     _stickNoteState.value = StickNoteState.Success(listStickNote)
                 }
         }
     }

  fun insertStickNote(stickyNoteDomain: StickyNoteDomain){
      viewModelScope.launch(Dispatchers.IO) {
          runCatching {
               insertStickNoteUseCase.insert(stickyNoteDomain)
          }.fold(
              onSuccess = {
                  if (it != 0L){
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
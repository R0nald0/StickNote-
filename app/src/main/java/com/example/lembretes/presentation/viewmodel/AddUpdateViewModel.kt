package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNote
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.impl.InsertStickNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiAddScreen(
   val stickyNoteDomain: StickyNoteDomain
)



@HiltViewModel
class AddUpdateViewModel @Inject constructor(
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val insertStickNoteUseCase: InsertStickNoteUseCase,
    private val getStickyNoteUseCase: GetStickyNoteUseCase
) : ViewModel() {

    private val _addScreeUi = MutableStateFlow(UiAddScreen(StickyNote(id = null)))
    var addScreenUi : StateFlow<UiAddScreen> = _addScreeUi.asStateFlow()



    fun findById(id:Int){

        viewModelScope.launch {
           getStickyNoteUseCase.getStickNoteById(id)
               .catch {  }
               .collect{stickNote->
                   _addScreeUi.update {
                       it.copy(
                           stickyNoteDomain = stickNote
                       )
                   }
               }
        }
    }
    fun updateStickNote(stickyNoteDomain: StickyNoteDomain){

        viewModelScope.launch{
            runCatching {
                updateStickNoteUseCase.updateStickNote(stickNote = stickyNoteDomain)
            }.fold(
                onSuccess = {linesAfected->
                    if (linesAfected != 0){
                       // _stickNoteState.value = StickNoteState.Success("lembrete atualizado")
                    }
                    Log.i("_INFO", "updateStiickNote: $linesAfected")
                },
                onFailure = {error->
                   // _stickNoteState.value =StickNoteState.Error(message =  error.message ?: "Erro ao Atualizar Lembrete")
                }
            )
        }
    }
    fun insertStickNote(stickyNoteDomain: StickyNoteDomain){
         //Loading
        viewModelScope.launch {
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
}
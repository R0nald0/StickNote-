package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNote
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
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
    val stickyNoteDomain: StickyNoteDomain,
    val erros : Map<String,String>,
    val isSuccess :Boolean
)

@HiltViewModel
class AddUpdateViewModel @Inject constructor(
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val insertStickNoteUseCase: InsertStickNoteUseCase,
    private val getStickyNoteUseCase: GetStickyNoteUseCase,
    private val validateStickNoteUseCase: ValidateStickNoteUseCase
) : ViewModel() {

    private val _addScreeUi = MutableStateFlow(UiAddScreen(StickyNote(id = null, noticafitionId = 0), erros = emptyMap(),false))
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

    fun validateFieldStickNote(stickyNoteDomain: StickyNoteDomain){
          val result = validateStickNoteUseCase.validateFieldsStickNote(stickyNoteDomain.name,stickyNoteDomain.description,stickyNoteDomain.dateTime)
           if (result.isNotEmpty()){
               _addScreeUi.update {state ->
                   state.copy(erros = result, isSuccess = false)
               }
               return
           }
        _addScreeUi.update {state ->
            state.copy(erros = result, isSuccess = true)
         }
    }
    fun updateStickNote(stickyNoteDomain: StickyNoteDomain,onAction:()->Unit){

        viewModelScope.launch{
            runCatching {
                validateFieldStickNote(stickyNoteDomain)

                if (_addScreeUi.value.erros.isNotEmpty() == true)return@launch
                updateStickNoteUseCase.updateStickNote(stickNote = stickyNoteDomain)

            }.fold(
                onSuccess = {linesAfected->
                    if (linesAfected != 0){
                       // _stickNoteState.value = StickNoteState.Success("lembrete atualizado")
                    }
                    onAction()
                    Log.i("_INFO", "updateStiickNote: $linesAfected")
                },
                onFailure = {error->
                   // _stickNoteState.value =StickNoteState.Error(message =  error.message ?: "Erro ao Atualizar Lembrete")
                }
            )
        }
    }
    fun insertStickNote(stickyNoteDomain: StickyNoteDomain,onAction:() -> Unit){
         //Loading
        viewModelScope.launch {
            runCatching {
                validateFieldStickNote(stickyNoteDomain)
                if (_addScreeUi.value.erros.isNotEmpty() == true)return@launch
                insertStickNoteUseCase.insert(stickyNoteDomain)
            }.fold(
                    onSuccess = {insertValue->
                    if (insertValue != 0L){
                        Log.i("INFO_", "insertStickNote: Inserido com sucesso $insertValue")
                    }
                    onAction()
                },
                onFailure = {erro->
                    Log.i("INFO_", "insertStickNote: Erro ao inserir $erro")
                }
            )
        }
    }
}
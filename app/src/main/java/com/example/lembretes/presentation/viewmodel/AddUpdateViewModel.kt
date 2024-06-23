package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.impl.InsertStickNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddUpdateViewModel @Inject constructor(
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val insertStickNoteUseCase: InsertStickNoteUseCase,
) : ViewModel() {
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
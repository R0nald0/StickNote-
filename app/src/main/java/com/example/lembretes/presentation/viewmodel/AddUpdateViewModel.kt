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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiAddScreen(
    val stickyNoteDomain: StickyNoteDomain,
    val error : String? = null,
    val validateFields: Map<String, String>,
    val isSuccess: Boolean,
)

@HiltViewModel
class AddUpdateViewModel @Inject constructor(
    private val updateStickNoteUseCase: UpdateStickNoteUseCase,
    private val insertStickNoteUseCase: InsertStickNoteUseCase,
    private val getStickyNoteUseCase: GetStickyNoteUseCase,
    private val validateStickNoteUseCase: ValidateStickNoteUseCase
) : ViewModel() {

    private val _addScreeUi = MutableStateFlow(
        UiAddScreen(
            StickyNote(id = null, noticafitionId = 0),
            validateFields = emptyMap(),
            error = null,
            isSuccess =false
        )
    )
    var addScreenUi: StateFlow<UiAddScreen> = _addScreeUi.asStateFlow()

    fun findById(id: Int) {
       /* _addScreeUi.update {
            it.copy(isLoading = true)
        }*/
        viewModelScope.launch {
            getStickyNoteUseCase.getStickNoteById(id)
                .catch {
                    /*_addScreeUi.update {
                        it.copy(isLoading = false)
                    }*/
                }
                .collect { stickNote ->
                    _addScreeUi.update {

                        it.copy(
                            stickyNoteDomain = stickNote,
                        )
                    }
                }
        }
    }

    fun validateFieldStickNote(name: String,description : String,dateTime: Long) {

        val result = validateStickNoteUseCase.validateFieldsStickNote(
            name,
            description,
            dateTime
        )
        if (result.isNotEmpty()) {
            _addScreeUi.update { state ->
                state.copy(validateFields = result, isSuccess = false)
            }
            return
        }
        _addScreeUi.update { state ->
            state.copy(validateFields = result, isSuccess = true)
        }
    }

    fun updateStickNote(stickyNoteDomain: StickyNoteDomain, onAction: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                val(_,name,description,dateTime) =stickyNoteDomain
                validateFieldStickNote(name = name, description = description, dateTime = dateTime)
                if (_addScreeUi.value.validateFields.isNotEmpty() == true){
                    onAction(false)
                    return@launch
                }
                updateStickNoteUseCase.updateStickNote(stickNote = stickyNoteDomain)

            }.fold(
                onSuccess = { linesAfected ->
                    onAction(true)
                },
                onFailure = { error ->
                    _addScreeUi.update {
                        it.copy(error = error.message ?: "Erro ao atualizar lembrete")
                    }
                    Log.e("_INFO", "updateStiickNote: $error")
                    // _stickNoteState.value =StickNoteState.Error(message =  error.message ?: "Erro ao Atualizar Lembrete")
                }
            )
        }
    }

    fun insertStickNote(stickyNoteDomain: StickyNoteDomain, onAction: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                val(_,name,description,dateTime) =stickyNoteDomain
                validateFieldStickNote(name = name, description = description, dateTime = dateTime)

                if (_addScreeUi.value.validateFields.isNotEmpty() == true){
                    onAction(false)
                    return@launch
                }
                insertStickNoteUseCase.insert(stickyNoteDomain)
            }.fold(
                onSuccess = { insertValue ->
                    onAction(true)
                },
                onFailure = { error ->
                    onAction(false)
                    _addScreeUi.update {
                        it.copy(error = error.message ?: "Algo deu errado,lembrete não criado,tente novamente")
                    }
                    Log.e("INFO_", "insertStickNote: Erro ao inserir $error")
                }
            )
        }
    }
    fun clearErroMessage() {
        _addScreeUi.update { it.copy(error = null) }
    }

}
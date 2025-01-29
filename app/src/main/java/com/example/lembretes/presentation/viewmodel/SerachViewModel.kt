package com.example.lembretes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiSearchState(
    val loading :Boolean = false,
    val resultSearches :List<StickyNoteDomain> = emptyList()
)

@HiltViewModel
class SerachViewModel @Inject constructor(
    private val getStickyNoteUseCaseImpl: GetStickyNoteUseCase,
) :ViewModel() {
    private val _uiSearchState = MutableStateFlow(UiSearchState())
    var uiSearchState :StateFlow<UiSearchState> = _uiSearchState.asStateFlow()

    fun findStickNotebyText(value:String){
        _uiSearchState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            getStickyNoteUseCaseImpl.getStickNoteByText(value)
                .collect{stickNotes ->
                    _uiSearchState.update {
                        delay(1000)
                        it.copy(
                            loading = false,
                            resultSearches = stickNotes
                        )
                    }
            }
        }
    }
}


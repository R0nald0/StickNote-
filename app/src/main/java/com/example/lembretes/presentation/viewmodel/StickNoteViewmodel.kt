package com.example.lembretes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StickNoteViewmodel @Inject constructor(
    private val getStickyNoteUseCaseImpl: GetStickyNoteUseCase
):ViewModel() {


}
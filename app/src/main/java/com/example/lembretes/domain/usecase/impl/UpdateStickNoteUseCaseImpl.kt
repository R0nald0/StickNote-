package com.example.lembretes.domain.usecase.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.UpdateStickNoteUseCase
import javax.inject.Inject

class UpdateStickNoteUseCaseImpl @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository
) : UpdateStickNoteUseCase {
    override
    suspend fun updateStickNote(stickNote: StickyNoteDomain): Int {
        return stickyNoteRepository.update(stickNote)
    }
}
package com.example.lembretes.domain.usecase.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.DeleteStickNoteUseCase
import javax.inject.Inject

class DeleteStickNoteUseCaseImpl @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository
) : DeleteStickNoteUseCase {
    override suspend fun deleteStickNote(stickyNoteDomain: StickyNoteDomain): Int {
       return stickyNoteRepository.delete(stickyNoteDomain)
    }
}
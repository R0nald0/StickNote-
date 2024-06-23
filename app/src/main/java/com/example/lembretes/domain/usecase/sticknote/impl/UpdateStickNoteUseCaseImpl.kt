package com.example.lembretes.domain.usecase.sticknote.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import javax.inject.Inject

class UpdateStickNoteUseCaseImpl @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository
) : UpdateStickNoteUseCase {
    override
    suspend fun updateStickNote(stickNote: StickyNoteDomain): Int {
        return stickyNoteRepository.update(stickNote)
    }

    override suspend fun updateNotificatioStickNote(idStickNote: Int, isRemember: Boolean)
                 = stickyNoteRepository.updateNotificatioStickNote(idStickNote, isRemember)
    }

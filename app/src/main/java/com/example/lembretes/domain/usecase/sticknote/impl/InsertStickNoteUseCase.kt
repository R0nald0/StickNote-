package com.example.lembretes.domain.usecase.sticknote.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import javax.inject.Inject

class InsertStickNoteUseCase @Inject constructor(
    private val stickyNoteRepository: StickyNoteRepository
) {

    suspend fun insert(stickyNoteDomain: StickyNoteDomain) : Long{
         return stickyNoteRepository.insert(stickyNoteDomain)
    }
}
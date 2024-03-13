package com.example.lembretes.domain.usecase.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStickyNoteUseCaseImpl @Inject constructor(
    private  val stickyNoteRepository: StickyNoteRepository
): GetStickyNoteUseCase {
    override suspend fun GetStickyNotes(): Flow<List<StickyNoteDomain>> {
        return stickyNoteRepository.getStickyNotes()
    }
}
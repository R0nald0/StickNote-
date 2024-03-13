package com.example.lembretes.domain.usecase

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.coroutines.flow.Flow


interface GetStickyNoteUseCase {
    suspend fun GetStickyNotes(): Flow<List<StickyNoteDomain>>
}
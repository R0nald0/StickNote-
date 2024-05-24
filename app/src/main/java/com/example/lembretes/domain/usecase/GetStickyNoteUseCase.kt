package com.example.lembretes.domain.usecase

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.coroutines.flow.Flow


interface GetStickyNoteUseCase {
    suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>>
    suspend fun getStickNotesToday():Flow<List<StickyNoteDomain>>
    suspend fun getStickNotesTomorrow():Flow<List<StickyNoteDomain>>
}
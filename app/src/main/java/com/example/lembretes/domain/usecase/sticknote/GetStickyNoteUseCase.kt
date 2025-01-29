package com.example.lembretes.domain.usecase.sticknote

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.coroutines.flow.Flow


interface GetStickyNoteUseCase {
    suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>>
    suspend fun getStickNoteById(id: Int):Flow<StickyNoteDomain>
    suspend fun getStickNotesToday():Flow<List<StickyNoteDomain>>
    suspend fun getStickNotesTomorrow():Flow<List<StickyNoteDomain>>
    suspend fun getStickNoteByText(value : String):Flow<List<StickyNoteDomain>>
}
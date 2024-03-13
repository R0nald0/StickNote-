package com.example.lembretes.domain.repository

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.coroutines.flow.Flow

interface StickyNoteRepository {
    suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>>
    suspend fun insert(stickyNoteDomain: StickyNoteDomain):Long

}
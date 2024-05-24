package com.example.lembretes.domain.repository

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.coroutines.flow.Flow

interface StickyNoteRepository {
    suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>>
    suspend fun getStickByPeriodicDate(firstDate :Long, secondDate:Long):Flow<List<StickyNoteDomain>>
    suspend fun insert(stickyNoteDomain: StickyNoteDomain):Long
    suspend fun update(stickyNoteDomain: StickyNoteDomain):Int
    suspend fun delete(stickyNoteDomain: StickyNoteDomain):Int
}
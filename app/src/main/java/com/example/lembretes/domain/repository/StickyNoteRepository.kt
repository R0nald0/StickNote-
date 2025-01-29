package com.example.lembretes.domain.repository

import com.example.lembretes.domain.model.StickyNoteDomain
import kotlinx.coroutines.flow.Flow

interface StickyNoteRepository {
    suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>>
    suspend fun getStickNoteById(id:Int):Flow<StickyNoteDomain>
    suspend fun getStickByPeriodicDate(firstDate :Long, secondDate:Long):Flow<List<StickyNoteDomain>>
    suspend fun getStickNoteByText(value :String):Flow<List<StickyNoteDomain>>
    suspend fun getStickNoteByTag(value :String):Flow<List<StickyNoteDomain>>
    suspend fun insert(stickyNoteDomain: StickyNoteDomain):Long
    suspend fun update(stickyNoteDomain: StickyNoteDomain):Int
    suspend fun updateNotificatioStickNote(idStickNote :Int,isRemember :Boolean)
    suspend fun delete(stickyNoteDomain: StickyNoteDomain):Int
}
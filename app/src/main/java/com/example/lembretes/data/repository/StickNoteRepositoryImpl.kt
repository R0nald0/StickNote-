package com.example.lembretes.data.repository

import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.entity.toStickNote
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.toLembrete
import com.example.lembretes.domain.repository.StickyNoteRepository
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class StickNoteRepositoryImpl @Inject constructor(
    private val lembreteDao: LembreteDao
) :StickyNoteRepository {
    override suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>> {
        return flowOf(lembreteDao.findAll().map {lembrete ->
            lembrete.toStickNote()
        })

    }

    override suspend fun insert(stickyNoteDomain: StickyNoteDomain): Long {
          return  lembreteDao.insertLembrete(stickyNoteDomain.toLembrete())
    }
}
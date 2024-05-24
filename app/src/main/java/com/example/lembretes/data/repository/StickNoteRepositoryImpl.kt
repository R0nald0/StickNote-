package com.example.lembretes.data.repository

import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.entity.toStickNote
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.toLembrete
import com.example.lembretes.domain.repository.StickyNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StickNoteRepositoryImpl @Inject constructor(
    private val lembreteDao: LembreteDao
) :StickyNoteRepository {
    override suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>> {
        return lembreteDao.findAll().map {stickNoteList->
                  stickNoteList.map { lembrete -> lembrete.toStickNote()}
         }

    }

    override suspend fun getStickByPeriodicDate(firstDate: Long, secondDate: Long):Flow<List<StickyNoteDomain>> {
        try {
          return  lembreteDao.findStickNoteByDate(firstDate, secondDate).map {notesPeriodic->
                 notesPeriodic.map {
                     it.toStickNote()
                 }
            }
        }catch (e:Exception){
            e.printStackTrace()
            throw (e)
        }
    }

    override suspend fun insert(stickyNoteDomain: StickyNoteDomain): Long {
        return lembreteDao.insertLembrete(stickyNoteDomain.toLembrete())
    }

    override suspend fun update(stickyNoteDomain: StickyNoteDomain): Int {
       try {
           return  lembreteDao.update(stickyNoteDomain.toLembrete())
       }catch (ex :Exception){
           throw ex;
       }

    }

    override suspend fun delete(stickyNoteDomain: StickyNoteDomain): Int {
        return  lembreteDao.deleteLembrete(stickyNoteDomain.toLembrete())
    }
}
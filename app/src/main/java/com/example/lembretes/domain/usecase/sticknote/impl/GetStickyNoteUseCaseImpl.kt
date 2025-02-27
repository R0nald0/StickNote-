package com.example.lembretes.domain.usecase.sticknote.impl

import android.util.Log
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.utils.dateTimeTomorow
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class GetStickyNoteUseCaseImpl @Inject constructor(
    private  val stickyNoteRepository: StickyNoteRepository
): GetStickyNoteUseCase {
    override suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>> {
        return stickyNoteRepository.getStickyNotes()
    }

    override suspend fun getStickNoteById(id: Int) = stickyNoteRepository.getStickNoteById(id)

    override suspend fun getStickNotesToday(): Flow<List<StickyNoteDomain>> {

        val currenDate = Calendar.getInstance().apply {
            set(
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH),
                0, 0, 0)
        }

        val finalDate = Calendar.getInstance().apply {
            set(
               get(Calendar.YEAR),
               get(Calendar.MONTH),
               get(Calendar.DAY_OF_MONTH),
            23, 59, 59)
        }


        val firsEPock =  Date().dateTimeTomorow(currenDate.timeInMillis,0)
        val secondEPock =   Date().dateTimeTomorow(finalDate.timeInMillis,0)

        Log.i("_INFO", "getStickNotesToday:First $firsEPock ")
        Log.i("_INFO", "getStickNotesToday:Second $secondEPock ")

        return  stickyNoteRepository.getStickByPeriodicDate(
            firstDate = currenDate.timeInMillis,
            secondDate =  finalDate.timeInMillis
        )
    }

    override suspend fun getStickNotesTomorrow(): Flow<List<StickyNoteDomain>> {

        val newCurrenDate = Calendar.getInstance().apply {
            set(
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH),
                0, 0, 0)
        }

        val finalDate = Calendar.getInstance().apply {
            set(
                 get(Calendar.YEAR),
                 get(Calendar.MONTH),
                 get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59)
        }
       val firsEPock = Instant.fromEpochMilliseconds(newCurrenDate.timeInMillis)
             .plus(1.days).toEpochMilliseconds()
        val secondEPock =  Instant.fromEpochMilliseconds(finalDate.timeInMillis)
            .plus(1.days).toEpochMilliseconds()

        return  stickyNoteRepository.getStickByPeriodicDate(
           firstDate = firsEPock,
           secondDate =  secondEPock
        )
    }

    override suspend fun getStickNoteByText(value: String) : Flow<List<StickyNoteDomain>> {
        return if (value.contains('#')){
            stickyNoteRepository.getStickNoteByTag(value)
        }else{
            stickyNoteRepository.getStickNoteByText(value)
        }
    }
}
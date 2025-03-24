package com.example.lembretes.domain.usecase.sticknote.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.utils.getDateFronLongOfCurrentSystemDate
import com.example.lembretes.utils.getDateInLocalDateFronTimeUTC
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.Calendar
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

        val currenLocalDate = Clock.System.getDateInLocalDateFronTimeUTC(System.currentTimeMillis()).date
        val currenDate = createLocalDateTime(currenLocalDate, LocalTime(0,0,0))

        val localDate = Clock.System.getDateFronLongOfCurrentSystemDate(System.currentTimeMillis()).date
         val finalDate   = createLocalDateTime(localDate,LocalTime(23,59,59))

        val firsEPock =   currenDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val secondEPock = finalDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        return  stickyNoteRepository.getStickByPeriodicDate(
            firstDate = firsEPock,
            secondDate =  secondEPock
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

   private  fun createLocalDateTime(localDate: LocalDate,localTime: LocalTime): LocalDateTime{
       return LocalDateTime(localDate, localTime)
    }

}
package com.example.lembretes.domain.usecase.impl

import app.cash.turbine.test
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.sticknote.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.utils.convertDateStringToLong
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date


@RunWith(MockitoJUnitRunner::class)
class GetStickyNoteUseCaseImplTest {


    @Mock
    lateinit var  stickRepository : StickyNoteRepository

    lateinit var getStickyNoteUseCaseImpl: GetStickyNoteUseCaseImpl
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getStickyNoteUseCaseImpl= GetStickyNoteUseCaseImpl(stickRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getStickyNotes_must_find_all_StickNotes() = runTest {
        Mockito.`when`(stickRepository.getStickyNotes()).thenReturn(flowOf(buildListLStickNotes()))
          getStickyNoteUseCaseImpl.getStickyNotes().collect{ listStickNote->
             assertThat(listStickNote).hasSize(3)
             assertThat(listStickNote[0]).isInstanceOf(StickyNoteDomain::class.java)
             assertThat(listStickNote[2].name).isEqualTo("desenhar")
        }

    }

    @Test
    fun getStickNotesToday_must_return_stickNotes_of_today()=runTest{
         Mockito.`when`(stickRepository.getStickByPeriodicDate(Mockito.anyLong(),Mockito.anyLong())).thenReturn(
             flowOf(buildListLStickNotes())
         )

        getStickyNoteUseCaseImpl.getStickNotesToday().test {
             val stickNotes = awaitItem()
             awaitComplete()
            //  println("final date: ${Date().dateTimeTomorow(stickNo)}")
             assertThat(stickNotes.size).isEqualTo(3)
             cancel()
        }
    }

    @Test
    fun getStickNotesTomorrow_must_return_stickNotes_of_tomorow()=runTest{
        Mockito.`when`(stickRepository.getStickByPeriodicDate(Mockito.anyLong(),Mockito.anyLong())).thenReturn(
            flowOf(buildListLStickNotes())
        )

        getStickyNoteUseCaseImpl.getStickNotesToday().test {
            val stickNotes = awaitItem()
            awaitComplete()
            assertThat(stickNotes.size).isEqualTo(3)
            assertThat(stickNotes).isInstanceOf(List::class.java)
            cancel()
        }
    }
    private fun buildListLStickNotes()= listOf(
        StickyNoteDomain(
            id = 1,
            name = "ler e caminhar",
            description = "caminhar duas horas e ler",
            dateTime =  Date().convertDateStringToLong("20/05/2025")!!,
            isRemember = false
        ),
        StickyNoteDomain(
            id = 2,
            name = "ler e caminhar",
            description = "estudar kotlin",
            dateTime =  Date().convertDateStringToLong("21/05/2025")!!,
            isRemember = true
        ),
        StickyNoteDomain(
            id = 3,
            name = "desenhar",
            description = "desenhar algo",
            dateTime =  Date().convertDateStringToLong("20/05/2025")!!,
            isRemember = false
        )
    )
}
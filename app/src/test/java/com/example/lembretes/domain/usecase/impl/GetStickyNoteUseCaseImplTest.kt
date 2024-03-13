package com.example.lembretes.domain.usecase.impl

import com.example.lembretes.data.entity.LembreteEntity
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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

          getStickyNoteUseCaseImpl.GetStickyNotes().collect{listStickNote->
             assertThat(listStickNote).hasSize(3)
             assertThat(listStickNote[0]).isInstanceOf(StickyNoteDomain::class.java)
             assertThat(listStickNote[2].name).isEqualTo("desenhar")
        }

    }

    private fun buildListLStickNotes()= listOf(
        StickyNoteDomain(
            id = 1,
            name = "ler e caminhar",
            description = "caminhar duas horas e ler",
            dateTime = Date().time,
            isRemember = false
        ),
        StickyNoteDomain(
            id = 2,
            name = "ler e caminhar",
            description = "estudar kotlin",
            dateTime = Date().time,
            isRemember = true
        ),
        StickyNoteDomain(
            id = 3,
            name = "desenhar",
            description = "desenhar algo",
            dateTime = Date().time,
            isRemember = false
        )
    )
}
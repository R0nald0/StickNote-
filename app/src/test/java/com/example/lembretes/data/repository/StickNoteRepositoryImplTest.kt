package com.example.lembretes.data.repository

import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.entity.LembreteEntity

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.model.toLembrete

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
class StickNoteRepositoryImplTest {

    @Mock
    lateinit var  lembreteDao : LembreteDao

    lateinit var stickNoteRepositoryImpl: StickNoteRepositoryImpl
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        stickNoteRepositoryImpl = StickNoteRepositoryImpl(lembreteDao)
    }
    @Test
    fun getStickyNotes_must_findAll_stickNote() = runTest {
       Mockito.`when`(lembreteDao.findAll()).thenReturn(buildListLStickNotes())

       stickNoteRepositoryImpl.getStickyNotes().collect{ listStick->
          assertThat(listStick.size).isEqualTo(3)
          assertThat(listStick[0]).isInstanceOf(StickyNoteDomain::class.java)
          assertThat(listStick[0].name).isEqualTo("ler e caminhar")
        }

    }

    @Test
    fun insert_must_add_stickNote_in_list() = runTest {
        val stick = StickyNoteDomain(
            -1,"beber agua","beber aguaa a cafa 15 minutos",Date().time,false)

        Mockito.`when`(lembreteDao.insertLembrete(stick.toLembrete())).thenReturn(1)
        val result = stickNoteRepositoryImpl.insert(stick)

        assertThat(result).isEqualTo(1)
    }

    @After
    fun tearDown() {
    }

    private fun buildListLStickNotes()= listOf(
        LembreteEntity(
            id = 1,
            name = "ler e caminhar",
            description = "caminhar duas horas e ler",
            dateTime = Date().time,
            isRemember = false
        ),
        LembreteEntity(
            id = 2,
            name = "ler e caminhar",
            description = "estudar kotlin",
            dateTime = Date().time,
            isRemember = true
        ),
        LembreteEntity(
            id = 3,
            name = "desenhar",
            description = "desenhar algo",
            dateTime = Date().time,
            isRemember = false
        )
    )
}
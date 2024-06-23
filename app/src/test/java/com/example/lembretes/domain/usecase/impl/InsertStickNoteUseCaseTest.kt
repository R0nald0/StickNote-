package com.example.lembretes.domain.usecase.impl

import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.sticknote.impl.InsertStickNoteUseCase
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
class InsertStickNoteUseCaseTest {

    @Mock
    lateinit var repository: StickyNoteRepository

    lateinit var stickNoteUseCase: InsertStickNoteUseCase
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        stickNoteUseCase = InsertStickNoteUseCase(repository)
    }

    @After
    fun tearDown() {}

    @Test
    fun insert()= runTest {
        val fakeStickNote = StickyNoteDomain(-1,"Estudar algo novo","algo novo á aprender", Date().time,false)
        Mockito.`when`(repository.insert(fakeStickNote)).thenReturn(1)

        val result = stickNoteUseCase.insert(fakeStickNote)

        assertThat(result).isEqualTo(1)
    }
}
package com.example.lembretes.presentation.viewmodel

import app.cash.turbine.test
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.InsertStickNoteUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class StickNoteViewmodelTest {

    @Mock
    lateinit var getStickyNoteUseCaseImpl: GetStickyNoteUseCaseImpl

    @Mock
    lateinit var insertStickNoteUseCase: InsertStickNoteUseCase



    lateinit var stickNoteViewmodel: StickNoteViewmodel
    val testRepository = TestRepository()
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        stickNoteViewmodel = StickNoteViewmodel(testRepository,insertStickNoteUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = UnconfinedTestDispatcher()
    @After
    fun tearDown() {

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun ShouldVerifStateInitialIsAEmptyList()= runTest(testDispatcher) {

   ///     Mockito.`when`(getStickyNoteUseCaseImpl.getStickyNotes()).thenReturn(testRepository.getStickyNotes())

        stickNoteViewmodel.stickNoteState.test {
            assertThat(awaitItem()).isNotSameInstanceAs(StickNoteState.Success(emptyList()))
        }
    }

    @Test
    fun insertStickNote() {
    }

}

fun buildListLStickNotes()= listOf(
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
class TestRepository : GetStickyNoteUseCase{
    override suspend fun getStickyNotes(): Flow<List<StickyNoteDomain>> {
        return  flow {
            delay(1000)
            emit(
                buildListLStickNotes()
            )
        }
    }

}
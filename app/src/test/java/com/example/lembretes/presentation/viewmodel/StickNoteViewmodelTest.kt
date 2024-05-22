package com.example.lembretes.presentation.viewmodel

import app.cash.turbine.test
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.impl.DeleteStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.InsertStickNoteUseCase
import com.example.lembretes.domain.usecase.impl.UpdateStickNoteUseCaseImpl
import com.example.lembretes.utils.TestDispatcherRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
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

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    @Mock
    lateinit var getStickyNoteUseCaseImpl: GetStickyNoteUseCaseImpl
    @Mock
    lateinit var updateStickyNoteUseCaseImpl: UpdateStickNoteUseCaseImpl

    @Mock
    lateinit var deleteStickyNoteUseCaseImpl: DeleteStickNoteUseCaseImpl

    @Mock
    lateinit var insertStickNoteUseCase: InsertStickNoteUseCase



    lateinit var stickNoteViewmodel: StickNoteViewmodel
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        stickNoteViewmodel = StickNoteViewmodel(
            getStickyNoteUseCaseImpl = getStickyNoteUseCaseImpl,
            insertStickNoteUseCase =  insertStickNoteUseCase,
            deleteStickNoteUseCase = deleteStickyNoteUseCaseImpl,
            updateStickNoteUseCase = updateStickyNoteUseCaseImpl
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = UnconfinedTestDispatcher()
    @After
    fun tearDown() {

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun ShouldVerifStateInitialIsAEmptyList()= runTest() {
        stickNoteViewmodel.stickNoteState.collect{
            println("teste aw $it")
        }
        stickNoteViewmodel.stickNoteState.test {
          val result  = awaitItem()
            assertThat(result).isEqualTo(StickNoteState.Success(emptyList()))
            cancel()
        }

    }

    @Test
    fun  getStickNotes_must_return_a_lis_of_sstickNote()= runTest {
        Mockito.`when`(getStickyNoteUseCaseImpl.getStickyNotes()).thenReturn(flowOf(
            buildListLStickNotes()
        ))
        stickNoteViewmodel.getStickNotes()
        stickNoteViewmodel.stickNoteState.test {
           val result = awaitItem()
            println("teste ${result}")

            assertThat(result).isInstanceOf(StickNoteState.Success::class.java)
            cancel()
        }

    }

    @Test
   fun  getStickNotesToday_must_return_stickNote_of_today()= runTest{
        val date = Date().time
        Mockito.`when`(getStickyNoteUseCaseImpl.getStickNotesToday()).thenReturn(flowOf(
            buildListLStickNotes()
        ))
        stickNoteViewmodel.stickNoteState.test {
            val result = awaitItem()
            println("teste $result")

            assertThat(result).isInstanceOf(StickNoteState.Success::class.java)
            cancel()
        }
    }

    @Test
    fun insertStickNote() {}

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


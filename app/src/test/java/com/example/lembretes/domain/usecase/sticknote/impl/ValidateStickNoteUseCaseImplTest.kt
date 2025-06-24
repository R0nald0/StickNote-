package com.example.lembretes.domain.usecase.sticknote.impl

import androidx.room.util.copy
import com.example.lembretes.domain.model.StickyNote
import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import com.google.common.truth.Truth
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RunWith(MockitoJUnitRunner::class)
class ValidateStickNoteUseCaseImplTest {
     lateinit var validateStickNoteUseCaseImpl: ValidateStickNoteUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        validateStickNoteUseCaseImpl = ValidateStickNoteUseCaseImpl()
    }


    @Test
    fun `When validateFieldsStickNote is Executed,shuold retuns erro map contain item with key title`(){
         val stickyNoteDomain = stickyNotes[1].copy(name = "")

        val resul =  validateStickNoteUseCaseImpl.validateFieldsStickNote(stickyNoteDomain.name,stickyNoteDomain.description,stickyNoteDomain.dateTime)
        Truth.assertThat(resul).isNotEmpty()
        Truth.assertThat(resul.containsKey("title")).isTrue()
        Truth.assertThat(resul.getValue("title")).isEqualTo("O campo título precisa ser preenchido")
        Truth.assertThat(resul.size).isEqualTo(1)

    }
    @Test
    fun `When validateFieldsStickNote is Executed,should retuns erro map contained item with key description`(){
        val stickyNoteDomain = stickyNotes[1].copy(description = "")

        val resul =  validateStickNoteUseCaseImpl.validateFieldsStickNote(stickyNoteDomain.name,stickyNoteDomain.description,stickyNoteDomain.dateTime)
        Truth.assertThat(resul).isNotEmpty()
        Truth.assertThat(resul.containsKey("description")).isTrue()
        Truth.assertThat(resul.getValue("description")).isEqualTo("O campo descrição precisa ser preenchido")
        Truth.assertThat(resul.size).isEqualTo(1)
    }

    @Test
    fun `When validateFieldsStickNote is Executed,shuold return map of string contained item with key date`(){
        val stickyNoteDomain = stickyNotes[0]

        val resul =  validateStickNoteUseCaseImpl.validateFieldsStickNote(stickyNoteDomain.name,stickyNoteDomain.description,stickyNoteDomain.dateTime)
        Truth.assertThat(resul).isNotEmpty()
        Truth.assertThat(resul.containsKey("date")).isTrue()
        Truth.assertThat(resul.getValue("date")).isEqualTo("Data inválida")
        Truth.assertThat(resul.size).isEqualTo(1)
    }
}




private val stickyNotes = listOf(
    StickyNote(
        id = 1,
        name = "Comprar mantimentos",
        description = "Comprar leite, pão e ovos no mercado",
        dateTime = System.currentTimeMillis() - 3600000,
        isRemember = true,
        tags = mutableListOf("Compras", "Mercado"),
        noticafitionId = 123
    ),
    StickyNote(
        id = 2,
        name = "Reunião de equipe",
        description = "Reunião semanal para discutir o progresso do projeto",
        dateTime = System.currentTimeMillis() + 3600000,
        isRemember = true,
        tags = mutableListOf("Trabalho", "Reunião"),
        noticafitionId = 321
    ),
    StickyNote(
        id = 3,
        name = "Treino na academia",
        description = "Sessão de musculação e corrida",
        dateTime = System.currentTimeMillis() + 7200000,
        isRemember = false,
        tags = mutableListOf("Saúde", "Exercício"),
        noticafitionId = 32321
    )
)
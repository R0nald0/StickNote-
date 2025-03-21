package com.example.lembretes.domain.usecase.sticknote.impl

import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ValidateStickNoteUseCaseImpl :ValidateStickNoteUseCase {
    override fun validateFieldsStickNote(
        title: String,
        description: String,
        date: Long?
    ): Map<String, String> {
        val erros = mutableMapOf<String,String>()
        if (title.isBlank()) {
            erros.putAll(mapOf("title" to "O campo título precisa ser preenchido"))
        }
        if (description.isBlank()) {
           erros.putAll( mapOf("description" to "O campo descrição precisa ser preenchido"))
        }

        if (date == null || date == 0L) {
            erros.putAll(mapOf("date" to "Data inválida"))
            return erros
        }

        val actualDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val dateChosedByUser =Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.UTC)

        if (   dateChosedByUser  < actualDate ) {
            erros.putAll( mapOf("date" to "Data inválida"))
        }

     return  erros
    }
    override fun validateUpdateNotifcation(date: Long): Boolean{
        val actualDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val dateChosedByUser =Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.UTC)

        return actualDate < dateChosedByUser

    }
}
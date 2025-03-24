package com.example.lembretes.domain.usecase.sticknote.impl

import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import com.example.lembretes.utils.getDateFronLongOfCurrentSystemDate
import com.example.lembretes.utils.getDateInLocalDateFronTimeUTC
import com.example.lembretes.utils.getDateNowInLocalDateTime
import kotlinx.datetime.Clock

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

        val actualDate = Clock.System.getDateNowInLocalDateTime()
        val dateChosedByUser = Clock.System.getDateInLocalDateFronTimeUTC(date)

        if (   dateChosedByUser  < actualDate ) {
            erros.putAll( mapOf("date" to "Data inválida"))
        }

     return  erros
    }
    override fun validateUpdateNotifcation(date: Long): Boolean{
        val actualDate = Clock.System.getDateNowInLocalDateTime()
        val dateChosedByUser = Clock.System.getDateFronLongOfCurrentSystemDate(date)
        return actualDate < dateChosedByUser
    }
}
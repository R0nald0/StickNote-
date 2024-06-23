package com.example.lembretes.presentation.model

import com.example.lembretes.utils.dateForExtense
import java.util.Date

enum class StickNoteEnumFilterType(val value: String) {
    Today(value = Date().dateForExtense()),All(value = "Todos"),TOMORROW(value = "Amanhâ")

}
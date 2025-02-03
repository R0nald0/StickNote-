package com.example.lembretes.domain.model

import com.example.lembretes.data.entity.LembreteEntity
typealias StickyNoteDomain =StickyNote

data class StickyNote(
    val id : Int?,
    val name :String ="",
    val description :String ="",
    val dateTime: Long = 0,
    var isRemember : Boolean =false,
    val tags:MutableList<String> = mutableListOf()
)

fun StickyNote.toLembrete() = LembreteEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    dateTime = this.dateTime,
    isRemember = this.isRemember,
    tags = this.tags

)




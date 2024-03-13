package com.example.lembretes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lembretes.domain.model.StickyNoteDomain
import java.time.LocalDateTime
typealias LembreteEntity = Lembrete
@Entity
data class Lembrete(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val name :String,
    val description :String,
    val dateTime: Long,
    val isRemember : Boolean
)

fun LembreteEntity.toStickNote() = StickyNoteDomain(
    id = this.id,
    name = this.name,
    description = this.description,
    dateTime = this.dateTime,
    isRemember =this.isRemember
)
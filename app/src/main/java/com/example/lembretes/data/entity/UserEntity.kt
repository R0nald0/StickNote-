package com.example.lembretes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lembretes.domain.model.UserDomain

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val  id : Int?,
    val name :String,
    val photoPerfil:String
)

fun UserEntity.toUser()= UserDomain(
    id = this.id!!  ,
    name = this.name,
    photoProfile = this.photoPerfil
)

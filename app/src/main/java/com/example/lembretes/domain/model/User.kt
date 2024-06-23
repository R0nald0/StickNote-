package com.example.lembretes.domain.model

import com.example.lembretes.data.entity.UserEntity

typealias UserDomain = User

data class User (
    val id :Int,
    val name :String,
    val photoProfile:String
){
    constructor():this(
        name = "",
        id = 0,
        photoProfile = ""
    )
}

fun UserDomain.toEntity() =UserEntity(
    name = this.name,
    id = this.id,
    photoPerfil = this.photoProfile
)
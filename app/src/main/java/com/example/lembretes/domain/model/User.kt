package com.example.lembretes.domain.model

import com.example.lembretes.data.entity.UserEntity
import com.google.gson.Gson

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

    constructor(json: String):this (
       name = "",
        id =0,
        photoProfile =""

    )
    companion object{
      fun  fromJson(json : String): User? = Gson().fromJson<User>(json, User::class.java)
    }


}
fun User.toJson() : String{
    return Gson().toJson(this)
}

fun UserDomain.toEntity() =UserEntity(
    name = this.name,
    id = this.id,
    photoPerfil = this.photoProfile
)
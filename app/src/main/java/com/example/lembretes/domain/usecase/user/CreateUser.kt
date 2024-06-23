package com.example.lembretes.domain.usecase.user

import com.example.lembretes.domain.model.UserDomain

interface CreateUser {
    suspend fun createUser(userDomain: UserDomain)
}
package com.example.lembretes.domain.usecase.user

import com.example.lembretes.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow

interface FindUser {
    suspend fun findFistUser():Flow<UserDomain>
}
package com.example.lembretes.domain.usecase.user.impl

import com.example.lembretes.domain.model.UserDomain
import com.example.lembretes.domain.repository.UserStickNoteRepository
import com.example.lembretes.domain.usecase.user.CreateUser
import javax.inject.Inject

class CreateUserImpl @Inject constructor (
    private  val userStickNoteRepository: UserStickNoteRepository
):CreateUser {
    override suspend fun createUser(userDomain: UserDomain) = userStickNoteRepository.createUser(userDomain)
}
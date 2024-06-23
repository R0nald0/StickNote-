package com.example.lembretes.domain.usecase.user.impl

import com.example.lembretes.domain.model.UserDomain
import com.example.lembretes.domain.repository.UserStickNoteRepository
import com.example.lembretes.domain.usecase.user.FindUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindUserImpl @Inject constructor(
    private val userStickNoteRepository: UserStickNoteRepository
): FindUser {
    override suspend fun findFistUser(): Flow<UserDomain> = userStickNoteRepository.findFistUser()
}
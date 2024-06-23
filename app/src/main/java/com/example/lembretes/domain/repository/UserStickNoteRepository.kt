package com.example.lembretes.domain.repository

import com.example.lembretes.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow

interface UserStickNoteRepository  {
  suspend fun findFistUser(): Flow<UserDomain>

  suspend fun createUser(userDomain: UserDomain)

  suspend fun updateUser(userDomain: UserDomain):Int

  suspend fun deleteUser(userDomain: UserDomain):Int
}
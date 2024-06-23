package com.example.lembretes.data.repository

import android.util.Log
import com.example.lembretes.core.AuthUserException
import com.example.lembretes.data.dao.UserDao
import com.example.lembretes.data.entity.toUser
import com.example.lembretes.domain.model.UserDomain
import com.example.lembretes.domain.model.toEntity
import com.example.lembretes.domain.repository.UserStickNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserStickNoteRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserStickNoteRepository {
    private val TAG = "_INFO"
    override suspend fun findFistUser(): Flow<UserDomain> {
        try {
            return userDao.findFistUser().map { it.toUser() }
        }catch (authUser:AuthUserException){
           throw  authUser
        }
    }

    override suspend fun createUser(userDomain: UserDomain) {
         try {
             userDao.createUser(userDomain.toEntity())
         }catch (authUser:AuthUserException){
             throw  authUser
         }
    }

    override suspend fun updateUser(userDomain: UserDomain): Int {
        try {
           return userDao.updateUser(userDomain.toEntity())
        }catch (authUser:AuthUserException){
            Log.i(TAG, "updateUser:${authUser.message} ")
            throw  authUser
        }
    }

    override suspend fun deleteUser(userDomain: UserDomain): Int {
        try {
            return userDao.deleteUser(userDomain.toEntity())
        }catch (authUser:AuthUserException){
            Log.i(TAG, "updateUser:${authUser.message} ")
            throw  authUser
        }
    }
}
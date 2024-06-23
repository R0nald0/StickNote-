package com.example.lembretes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lembretes.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

  @Query(value = "SELECT *FROM user WHERE id = 1")
   fun findFistUser() :Flow<UserEntity>
  @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun createUser(user: UserEntity)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun updateUser(user: UserEntity):Int

  @Delete
 suspend  fun deleteUser(user: UserEntity):Int

}
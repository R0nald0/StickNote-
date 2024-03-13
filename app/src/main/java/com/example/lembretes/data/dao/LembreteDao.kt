package com.example.lembretes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lembretes.data.entity.Lembrete

@Dao
interface LembreteDao {
  @Query("SELECT * FROM Lembrete")
  suspend fun findAll():List<Lembrete>
  @Insert
  fun insertLembrete( lembrete: Lembrete):Long
  @Update
  fun update(vararg lembrete: Lembrete):Int

  @Delete
  fun deleteLembrete(vararg lembrete: Lembrete):Int
}
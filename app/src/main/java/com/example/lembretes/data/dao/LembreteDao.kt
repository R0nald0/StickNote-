package com.example.lembretes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lembretes.data.entity.Lembrete
import com.example.lembretes.data.entity.LembreteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LembreteDao {
  @Query("SELECT * FROM Lembrete ORDER BY dateTime DESC ")
   fun findAll():Flow<List<Lembrete>>

  @Query("SELECT *FROM lembrete WHERE dateTime BETWEEN :firstDate AND :secondDate ORDER BY dateTime DESC")
  fun findStickNoteByDate(firstDate :Long ,secondDate:Long):Flow<List<Lembrete>>
  @Insert
 suspend fun insertLembrete(  lembrete: LembreteEntity):Long
  @Update
 suspend fun update( lembrete: LembreteEntity):Int

  @Delete
 suspend fun deleteLembrete(  lembrete: LembreteEntity):Int
}
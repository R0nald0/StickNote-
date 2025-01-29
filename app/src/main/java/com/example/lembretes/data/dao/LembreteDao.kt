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
  @Query("SELECT * FROM lembrete ORDER BY dateTime DESC ")
  suspend  fun findAll():List<Lembrete>

  @Query("SELECT * FROM  lembrete WHERE id = :id ")
    fun findById(id:Int):Flow<LembreteEntity>


  @Query("SELECT *FROM lembrete WHERE dateTime BETWEEN :firstDate AND :secondDate ORDER BY dateTime DESC")
  suspend fun findStickNoteByDate(firstDate :Long ,secondDate:Long):List<Lembrete>

  @Query("SELECT *FROM lembrete WHERE name LIKE '%'||:value||'%'")
   fun findStickNoteByText(value: String):Flow<List<LembreteEntity>>

   @Query("SELECT *FROM lembrete WHERE tags LIKE '%'||:value||'%'")
    fun findStickNoteByTag(value: String):Flow<List<LembreteEntity>>


 @Insert
 suspend fun insertLembrete(  lembrete: LembreteEntity):Long
  @Update
 suspend fun update( lembrete: LembreteEntity):Int

 @Query("UPDATE lembrete SET isRemember =:isNotification WHERE id = :idStickNote")
 suspend fun updateNotificatioStickNote(idStickNote:Int, isNotification : Boolean)
  @Delete
 suspend fun deleteLembrete(  lembrete: LembreteEntity):Int
}
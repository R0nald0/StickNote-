package com.example.lembretes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.entity.LembreteEntity

@Database(
    entities = [LembreteEntity::class],
    version = 1
)

abstract class LembreteDatabase : RoomDatabase(){
    abstract fun lembreteDao():LembreteDao


    companion object{
        fun createDataBase(context : Context) :LembreteDatabase{
            return Room.databaseBuilder(
                context = context,
                klass = LembreteDatabase::class.java,
                name = "lembrete.db"
            ).build()
        }
    }
}



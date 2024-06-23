package com.example.lembretes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.dao.UserDao
import com.example.lembretes.data.entity.LembreteEntity
import com.example.lembretes.data.entity.UserEntity

@Database(
    entities = [LembreteEntity::class,UserEntity::class],
    version = 1,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)

abstract class LembreteDatabase : RoomDatabase(){
    abstract fun lembreteDao():LembreteDao
    abstract fun  userDao():UserDao

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



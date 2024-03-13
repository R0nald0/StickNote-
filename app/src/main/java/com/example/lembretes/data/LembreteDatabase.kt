package com.example.lembretes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.entity.LembreteEntity

@Database(
    entities = [LembreteEntity::class],
    version = 1
)
abstract class LembreteDatabase : RoomDatabase(){
    abstract fun lembreteDao():LembreteDao
}



package com.example.lembretes.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lembretes.data.LembreteDatabase
import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.repository.StickNoteRepositoryImpl
import com.example.lembretes.domain.usecase.impl.GetStickyNoteUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class classModule {

    @Provides
    fun getStickNoteUseCase(stickNoteRepositoryImpl: StickNoteRepositoryImpl) : GetStickyNoteUseCaseImpl{
        return  GetStickyNoteUseCaseImpl(stickNoteRepositoryImpl)
    }

    @Provides
    fun  provideStickNoteRepository(lembreteDao: LembreteDao):StickNoteRepositoryImpl{
         return  StickNoteRepositoryImpl(lembreteDao)
    }

    @Provides
    fun providesLembretesDao(lembreteDatabse: LembreteDatabase):LembreteDao{
        return  lembreteDatabse.lembreteDao()
    }
    @Singleton
    @Provides
    fun privideRoomDataBase(@ApplicationContext context: Context) :LembreteDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = LembreteDatabase::class.java,
            name = "lembrete.db"
        ).build()
    }
}
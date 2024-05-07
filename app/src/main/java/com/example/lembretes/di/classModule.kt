package com.example.lembretes.di

import android.content.Context
import com.example.lembretes.data.LembreteDatabase
import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.repository.StickNoteRepositoryImpl
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.impl.DeleteStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.InsertStickNoteUseCase
import com.example.lembretes.domain.usecase.impl.UpdateStickNoteUseCaseImpl
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
    fun getStickNoteUseCase(stickNoteRepositoryImpl: StickyNoteRepository) : GetStickyNoteUseCaseImpl{
        return  GetStickyNoteUseCaseImpl(stickNoteRepositoryImpl)
    }


    @Provides
    fun provideInsertStickNote(repositoryImpl: StickyNoteRepository) : InsertStickNoteUseCase{
         return InsertStickNoteUseCase(repositoryImpl)
    }

    @Provides
    fun provideUpdateStickNoteUseCaseimpl(stickyNoteRepository: StickyNoteRepository):UpdateStickNoteUseCaseImpl{
         return  UpdateStickNoteUseCaseImpl(stickyNoteRepository)
    }

    @Provides
    fun provideDeleteStickNoteUseCaseImpl(stickyNoteRepository: StickyNoteRepository):DeleteStickNoteUseCaseImpl{
        return DeleteStickNoteUseCaseImpl(stickyNoteRepository)
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
        return  LembreteDatabase.createDataBase(context)
    }
}
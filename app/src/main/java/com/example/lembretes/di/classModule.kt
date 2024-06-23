package com.example.lembretes.di

import android.content.Context
import com.example.lembretes.data.LembreteDatabase
import com.example.lembretes.data.dao.LembreteDao
import com.example.lembretes.data.dao.UserDao
import com.example.lembretes.data.repository.StickNoteRepositoryImpl
import com.example.lembretes.data.repository.UserStickNoteRepositoryImpl
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.repository.UserStickNoteRepository
import com.example.lembretes.domain.usecase.sticknote.impl.DeleteStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.sticknote.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.domain.usecase.sticknote.impl.InsertStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.impl.UpdateStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.user.impl.CreateUserImpl
import com.example.lembretes.domain.usecase.user.impl.FindUserImpl
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
    @Singleton
    fun provideCreateUser(userStickNoteRepository: UserStickNoteRepository) : CreateUserImpl {
        return  CreateUserImpl(userStickNoteRepository)
    }
    @Provides
    @Singleton
    fun provideFindUser(userStickNoteRepository: UserStickNoteRepository) : FindUserImpl {
        return  FindUserImpl(userStickNoteRepository)
    }
    @Provides
    fun getStickNoteUseCase(stickNoteRepositoryImpl: StickyNoteRepository) : GetStickyNoteUseCaseImpl {
        return  GetStickyNoteUseCaseImpl(stickNoteRepositoryImpl)
    }

    @Provides
    fun provideInsertStickNote(repositoryImpl: StickyNoteRepository) : InsertStickNoteUseCase {
         return InsertStickNoteUseCase(repositoryImpl)
    }

    @Provides
    fun provideUpdateStickNoteUseCaseimpl(stickyNoteRepository: StickyNoteRepository): UpdateStickNoteUseCaseImpl {
         return  UpdateStickNoteUseCaseImpl(stickyNoteRepository)
    }

    @Provides
    fun provideDeleteStickNoteUseCaseImpl(stickyNoteRepository: StickyNoteRepository): DeleteStickNoteUseCaseImpl {
        return DeleteStickNoteUseCaseImpl(stickyNoteRepository)
    }

    @Provides
    @Singleton
    fun  provideStickNoteRepository(lembreteDao: LembreteDao):StickNoteRepositoryImpl{
         return  StickNoteRepositoryImpl(lembreteDao)
    }
    fun provideUserStickNoteRepositoryImpl(userDao: UserDao):UserStickNoteRepository{
        return  UserStickNoteRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun providesLembretesDao(lembreteDatabse: LembreteDatabase):LembreteDao{
        return  lembreteDatabse.lembreteDao()
    }
    @Provides
    @Singleton
    fun provideUserDao(lembreteDatabse: LembreteDatabase):UserDao{
        return  lembreteDatabse.userDao()
    }

    @Singleton
    @Provides
    fun privideRoomDataBase(@ApplicationContext context: Context) :LembreteDatabase {
        return  LembreteDatabase.createDataBase(context)
    }
}
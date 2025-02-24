package com.example.lembretes.di

import com.example.lembretes.data.repository.StickNoteRepositoryImpl
import com.example.lembretes.data.repository.UserStickNoteRepositoryImpl
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.repository.UserStickNoteRepository
import com.example.lembretes.domain.usecase.sticknote.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.ValidateStickNoteUseCase
import com.example.lembretes.domain.usecase.sticknote.impl.DeleteStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.sticknote.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.domain.usecase.sticknote.impl.UpdateStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.sticknote.impl.ValidateStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.user.CreateUser
import com.example.lembretes.domain.usecase.user.FindUser
import com.example.lembretes.domain.usecase.user.impl.CreateUserImpl
import com.example.lembretes.domain.usecase.user.impl.FindUserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module()
@InstallIn(SingletonComponent::class)
interface InterfaceModule {

    @Binds
    fun validateStickNoteUseCase(validateStickNoteUseCaseImpl: ValidateStickNoteUseCaseImpl):ValidateStickNoteUseCase
    @Binds
    fun bindCreateUser(createUser: CreateUserImpl):CreateUser
    @Binds
    fun bindFindUser(findUser: FindUserImpl):FindUser

    @Binds
    fun bindUserStickNoteRepository(userStickNoteRepositoryImpl: UserStickNoteRepositoryImpl):UserStickNoteRepository

    @Binds
    fun bindStickNoteRepository(stickyNoteRepository: StickNoteRepositoryImpl):StickyNoteRepository

    @Binds
    fun bindGetStickNoteUseCase(getStickyNoteUseCaseImpl: GetStickyNoteUseCaseImpl): GetStickyNoteUseCase

    @Binds
    fun bindUpdateStickNote(updateStickNote: UpdateStickNoteUseCaseImpl): UpdateStickNoteUseCase

    @Binds
    fun bindDeleteStickNote(deleteStickNote : DeleteStickNoteUseCaseImpl): DeleteStickNoteUseCase
}
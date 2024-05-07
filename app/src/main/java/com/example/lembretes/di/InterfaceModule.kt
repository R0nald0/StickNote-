package com.example.lembretes.di

import com.example.lembretes.data.repository.StickNoteRepositoryImpl
import com.example.lembretes.domain.repository.StickyNoteRepository
import com.example.lembretes.domain.usecase.DeleteStickNoteUseCase
import com.example.lembretes.domain.usecase.GetStickyNoteUseCase
import com.example.lembretes.domain.usecase.UpdateStickNoteUseCase
import com.example.lembretes.domain.usecase.impl.DeleteStickNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.GetStickyNoteUseCaseImpl
import com.example.lembretes.domain.usecase.impl.UpdateStickNoteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module()
@InstallIn(SingletonComponent::class)
interface InterfaceModule {
    @Binds
    fun bindStickNoteRepository(stickyNoteRepository: StickNoteRepositoryImpl):StickyNoteRepository

    @Binds
    fun bindGetStickNoteUseCase(getStickyNoteUseCaseImpl: GetStickyNoteUseCaseImpl):GetStickyNoteUseCase

    @Binds
    fun bindUpdateStickNote(updateStickNote:UpdateStickNoteUseCaseImpl):UpdateStickNoteUseCase

    @Binds
    fun bindDeleteStickNote(deleteStickNote : DeleteStickNoteUseCaseImpl):DeleteStickNoteUseCase
}
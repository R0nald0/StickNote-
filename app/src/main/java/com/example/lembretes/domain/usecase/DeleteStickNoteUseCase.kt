package com.example.lembretes.domain.usecase

import com.example.lembretes.domain.model.StickyNoteDomain

interface DeleteStickNoteUseCase {
  suspend fun deleteStickNote(stickyNoteDomain: StickyNoteDomain):Int
}
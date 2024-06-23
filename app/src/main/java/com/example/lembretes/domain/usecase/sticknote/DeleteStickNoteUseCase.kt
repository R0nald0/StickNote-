package com.example.lembretes.domain.usecase.sticknote

import com.example.lembretes.domain.model.StickyNoteDomain

interface DeleteStickNoteUseCase {
  suspend fun deleteStickNote(stickyNoteDomain: StickyNoteDomain):Int
}
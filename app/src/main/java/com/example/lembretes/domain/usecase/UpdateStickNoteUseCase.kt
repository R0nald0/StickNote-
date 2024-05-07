package com.example.lembretes.domain.usecase

import com.example.lembretes.domain.model.StickyNoteDomain

interface UpdateStickNoteUseCase {

   suspend  fun updateStickNote(stickNote: StickyNoteDomain):Int
}
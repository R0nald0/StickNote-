package com.example.lembretes.domain.usecase.sticknote

import com.example.lembretes.domain.model.StickyNoteDomain

interface UpdateStickNoteUseCase {

   suspend  fun updateStickNote(stickNote: StickyNoteDomain):Int
   suspend  fun updateNotificatioStickNote(idStickNote :Int,isRemember :Boolean)
}
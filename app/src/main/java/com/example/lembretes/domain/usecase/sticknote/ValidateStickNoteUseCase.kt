package com.example.lembretes.domain.usecase.sticknote

interface ValidateStickNoteUseCase {
    fun validateFieldsStickNote(title:String,description:String,date:Long?):Map<String,String>
}
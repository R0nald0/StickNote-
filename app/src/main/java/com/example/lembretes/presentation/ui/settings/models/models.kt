package com.example.lembretes.presentation.ui.settings.models

data class ItemMenu(
    val title: String,
    val textOptionSelected: String,
    val action :() ->Unit
){
    
}
data class RadioButtonClass(
    val id: Int,
    val isSelected: Boolean,
    val title: String
)

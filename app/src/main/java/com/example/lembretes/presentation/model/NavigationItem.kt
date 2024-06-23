package com.example.lembretes.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItemDataClass (
    val icon : ImageVector,
    val label :String,
    val selected :Boolean,
    val onClick :  ()->Unit,
    val badge : String
)
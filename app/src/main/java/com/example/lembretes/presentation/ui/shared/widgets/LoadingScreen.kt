package com.example.lembretes.presentation.ui.shared.widgets
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    isLoading :( )->Unit,
    loading : Boolean = true
    ) {
    Box (
        modifier = modifier
            .fillMaxSize()
        , contentAlignment = Alignment.Center

    ){
        LaunchedEffect(key1 = Unit ) {
            isLoading()
        }
            CircularProgressIndicator()

    }

 }

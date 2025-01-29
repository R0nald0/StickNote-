package com.example.lembretes.presentation.ui.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.R
import com.example.lembretes.presentation.ui.theme.LembretesTheme

@Composable
fun StickNoteBoxSwipToDismiss(
    contentAlignment: Arrangement.Horizontal,
    backGroundcolor : Color,
    icon : Int,
    textTitle : String
) {
    Box(
        modifier = Modifier.clip(CircleShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(backGroundcolor)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = contentAlignment
        ) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(id = icon), contentDescription = "" , tint = Color.White)
            }
            Text(
                text = textTitle,
                color  = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
private fun StickNoteBoxSwipToDismissPrev() {
    LembretesTheme {
        StickNoteBoxSwipToDismiss(
            contentAlignment = Arrangement.SpaceAround,
            icon = R.drawable.baseline_mode_edit_24,
            textTitle = "Teste de Swipe",
            backGroundcolor = Color.Green
        )
    }
}
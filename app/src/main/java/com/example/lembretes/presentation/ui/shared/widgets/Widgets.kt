package com.example.lembretes.presentation.ui.shared.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun StickChips(
    modifier: Modifier = Modifier,
    label:String,
    isSelected :Boolean =false,
    colorBackGround : Color = Color.LightGray,
    colorText:Color = Color.Black ,
    onClick : ()->Unit ={ }
) {
   
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSelected) 10.dp else 0.dp
        ),
        onClick  = onClick
    ) {
        Text(
            text = label,
            color = colorText,
            modifier = if (isSelected) modifier
                .drawBehind {
                    drawRoundRect(
                        color = colorBackGround,
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                }
                .padding(8.dp)

            else modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(6.dp),
                style = if (isSelected)  MaterialTheme.typography.bodyMedium
                    .copy(
                       fontSize = 12.sp,
                       fontWeight = FontWeight.W900
            )
            else MaterialTheme.typography.bodySmall
                .copy(
                    color = Color.Gray,
                )
        )
    }
}









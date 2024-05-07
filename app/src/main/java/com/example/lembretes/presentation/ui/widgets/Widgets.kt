package com.example.lembretes.presentation.ui.widgets

import android.graphics.drawable.Icon
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lembretes.presentation.dateForExtense


@Composable
fun StickChips(
    modifier: Modifier = Modifier,
    label:String,
    isSelected :Boolean =false
) {

    Text(
        label,
        modifier = if (isSelected) modifier
            .drawBehind {
                drawRoundRect(color = Color.Gray, cornerRadius = CornerRadius(10.dp.toPx()))
            }
            .padding(8.dp)
        else modifier
            .background(color = Color.Transparent) ,
        style = if (isSelected)  MaterialTheme.typography.bodyMedium.copy(
            fontSize = 12.sp, fontWeight = FontWeight.Bold
        )
                 else MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.SemiBold,
                     color = Color.Gray
                 )
    )

}

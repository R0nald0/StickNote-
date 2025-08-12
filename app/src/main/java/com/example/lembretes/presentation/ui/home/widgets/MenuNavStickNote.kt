package com.example.lembretes.presentation.ui.home.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.shared.widgets.StickChips
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.HomeState

@Composable
fun IntroShowcaseScope.MenuNavStickNote(
    modifier: Modifier = Modifier,
    uiState: HomeState,
    onFilterType: (StickNoteEnumFilterType) -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        StickNoteEnumFilterType.entries.forEachIndexed { index, stickNoteEnumFilterType ->
           val initialIndex =when (stickNoteEnumFilterType){
                StickNoteEnumFilterType.Today -> 2
                StickNoteEnumFilterType.TOMORROW -> 3
                StickNoteEnumFilterType.All -> 4
            }
            val  showCaseText = when (stickNoteEnumFilterType){
             StickNoteEnumFilterType.Today -> "Filtre seus lembretes pela data de hoje"
             StickNoteEnumFilterType.TOMORROW -> "Filtre seus lembretes pela data Amanhâ"
             StickNoteEnumFilterType.All -> " e aqui visualize todos os lembretes criados"
         }
            StickChips(
                modifier = Modifier.introShowCaseTarget(
                    index = initialIndex,
                    style = ShowcaseStyle.Default.copy(
                        targetCircleColor = Color.White,
                        backgroundColor = Color(0xFF7C99AC),
                        backgroundAlpha = 0.98f
                    ),
                    content = {
                        Column {
                            Text(
                                text = showCaseText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = colorScheme.onPrimary,
                                    fontWeight = FontWeight.W600
                                )
                            )
                        }
                    }
                ),
                label = stickNoteEnumFilterType.value,
                isSelected = uiState.filterType == stickNoteEnumFilterType,
                colorBackGround = colorScheme.primary,
                colorText = colorScheme.onPrimaryContainer,
                onClick = {
                    onFilterType(stickNoteEnumFilterType)
                },
            )

            if (index < StickNoteEnumFilterType.entries.toTypedArray().lastIndex) {
                Spacer(modifier = Modifier.width(10.dp))
            }

        }
    }
}

@Preview
@Composable
private fun MenuNavStickNotePreview() {
    LembretesTheme {

    }
}
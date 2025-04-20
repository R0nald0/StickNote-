package com.example.lembretes.presentation.ui.home.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.shared.widgets.StickChips
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.HomeState

@Composable
 fun MenuNavStickNote(
    modifier: Modifier = Modifier,
    uiState: HomeState,
    onFilterType :(StickNoteEnumFilterType)->Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StickNoteEnumFilterType.entries.forEachIndexed { index, stickNoteEnumFilterType ->
            StickChips(
                label = stickNoteEnumFilterType.value,
                isSelected = uiState.filterType == stickNoteEnumFilterType,
                colorBackGround = MaterialTheme.colorScheme.primary,
                colorText = MaterialTheme.colorScheme.onPrimaryContainer,
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
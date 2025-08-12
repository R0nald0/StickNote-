package com.example.lembretes.presentation.ui.addsticknote.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lembretes.domain.model.StickyNoteDomain

@Composable
fun StickNoteCheckBox(
    modifier: Modifier = Modifier,
    stickyNoteDomain:StickyNoteDomain?,
    isChecked:(Boolean)-> Unit
) {
    var isRemember by rememberSaveable {
        mutableStateOf(stickyNoteDomain?.isRemember ?: false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isRemember = !isRemember
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isRemember,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.onPrimaryContainer,
                uncheckedColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onCheckedChange = { isCheck ->
                isRemember = isCheck
                isChecked(isRemember)
            })
        Text(text = "Ativar notificação?",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            )
    }
}
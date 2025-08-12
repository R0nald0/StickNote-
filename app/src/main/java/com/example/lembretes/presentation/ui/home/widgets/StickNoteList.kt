package com.example.lembretes.presentation.ui.home.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcaseScope
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.model.StickNoteEnumFilterType
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteCardView
import com.example.lembretes.presentation.viewmodel.HomeState

@Composable
internal fun IntroShowcaseScope.StateListStickNote(
    uiState: HomeState,
    modifier: Modifier,
    onNavigateToAddStickNote: () -> Unit,
    onUpdate: (StickyNoteDomain) -> Unit,
    context: Context,
    onDelete: (StickyNoteDomain) -> Unit,
    onUpdateStateNotificaion: (StickyNoteDomain?, Boolean) -> Unit
) {
    when {
        uiState.listData != null -> {
            val listStickNote = uiState.listData
            if (listStickNote.isEmpty()) {
                StickNoteNoContent(
                    modifier = modifier,
                    filterType = uiState.filterType,
                    onNavigateToAddStickNote = onNavigateToAddStickNote
                )
            } else {
                StickNoteStateLazyList(
                    stickNotes = listStickNote,
                    context = context,
                    onNavigateToAddStickNote = onUpdate,
                    onDelete = onDelete,
                    onUpdateStateNotification = onUpdateStateNotificaion
                )
            }
        }

        uiState.error != null -> {
            Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
        }

    }
}

@Composable
fun IntroShowcaseScope.StickNoteStateLazyList(
    modifier: Modifier = Modifier,
    context: Context,
    stickNotes: List<StickyNoteDomain>,
    onNavigateToAddStickNote: (StickyNoteDomain) -> Unit,
    onDelete: (StickyNoteDomain) -> Unit,
    onUpdateStateNotification: (StickyNoteDomain?, Boolean) -> Unit,
) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        items(
            stickNotes, key = { it.id!! }) { stickNote ->

            val suitToDismissState = rememberSwipeToDismissBoxState()

            LaunchedEffect(key1 = suitToDismissState.currentValue) {
                when (suitToDismissState.currentValue) {
                    SwipeToDismissBoxValue.StartToEnd -> onNavigateToAddStickNote(stickNote)
                    SwipeToDismissBoxValue.EndToStart -> onDelete(stickNote)
                    SwipeToDismissBoxValue.Settled -> {}
                }
            }

            MySwipe(

                onUpdateStateNotification ={ isUpdate ->
                    onUpdateStateNotification(stickNote, isUpdate)
                },
                stickNote = stickNote,
                context = context,
                dismissState = suitToDismissState
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MySwipe(
    modifier: Modifier = Modifier,
    onUpdateStateNotification: (Boolean) -> Unit,
    dismissState: SwipeToDismissBoxState,
    stickNote: StickyNoteDomain,
    context: Context,
) {
    var color = Color.Transparent
    var align = Arrangement.End
    var icon = R.drawable.delete_24
    var textTitle = ""

    when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            color = colorScheme.error
            align = Arrangement.End
            textTitle = "Deletar"
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            color = Color.Green
            align = Arrangement.Start
            textTitle = "Atualizar"
            icon = R.drawable.baseline_mode_edit_24
            LaunchedEffect(dismissState.currentValue) {
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> {}
    }
    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = dismissState,
        backgroundContent = {
            StickNoteBoxSwipToDismiss(
                contentAlignment = align,
                backGroundcolor = color,
                icon = icon,
                textTitle = textTitle
            )
        }) {
        StickNoteCardView(
            stickyNoteDomain = stickNote,
            onUpdateStateNotification = onUpdateStateNotification,
            modifier = modifier,
        )
    }
}

@Composable
fun IntroShowcaseScope.StickNoteNoContent(
    modifier: Modifier,
    filterType: StickNoteEnumFilterType,
    onNavigateToAddStickNote: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Você ainda não adicionou nenhum lembrete ${getTextNameSearch(filterType)}.",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorScheme.primary,
            ),
            onClick = {
                onNavigateToAddStickNote()
            }) {
            //IntroStickNoteBoxExemple()

            Text(
                modifier = Modifier.introShowCaseTarget(
                    index = 5,
                    style = ShowcaseStyle.Default.copy(
                        targetCircleColor = Color.White,
                        backgroundColor = Color(0xFF7C99AC),
                        backgroundAlpha = 0.98f
                    ),
                    content = {
                        Column {
                            Text(
                                text = "Aqui você pode iniciar a criação de lembretes,caso ainda não tenha crirado nehnum",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = colorScheme.onPrimary,
                                    fontWeight = FontWeight.W600
                                )
                            )
                        }
                    }
                ),
                color = colorScheme.onPrimaryContainer,
                text = "Criar lembrete",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun getTextNameSearch(type: StickNoteEnumFilterType): String {
    return when (type) {
        StickNoteEnumFilterType.Today -> "para hoje"
        StickNoteEnumFilterType.TOMORROW -> "para amanhã"
        else -> ""
    }
}
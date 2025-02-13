package com.example.lembretes.presentation.ui.home.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.home.HomeState
import com.example.lembretes.presentation.ui.home.StickNoteNoContent
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteCardView

@Composable
internal fun StateListStickNote(
    uiState: HomeState,
    modifier: Modifier,
    onNavigateToAddStickNote: () -> Unit,
    onUpdate: (StickyNoteDomain) -> Unit, //vefificar aqui
    context: Context,
    onDelete: (StickyNoteDomain) -> Unit,
    onUpdateStateNotificaion: (Int, Boolean) -> Unit
) {
    when {
        uiState.listData != null -> {
            val listStickNote = uiState.listData
            if (listStickNote.isEmpty()) {
                StickNoteNoContent(modifier, uiState.filterType, onNavigateToAddStickNote)
            } else {

                StickNoteStateLazyList(
                    stickNotes = listStickNote,
                    context = context,
                    onNavigateToAddStickNote = onUpdate,
                    onDelete = onDelete,
                    onUpdateStateNotificaion = onUpdateStateNotificaion
                )
            }
        }

        /*  uiState.isLoading -> {
              LoadingScreen(isLoading = { uiState.isLoading }, loading = uiState.isLoading )
          }*/

        uiState.erro != null -> {
            Toast.makeText(context, uiState.erro, Toast.LENGTH_SHORT).show()
        }

    }
}

@Composable
fun StickNoteStateLazyList(
    modifier: Modifier = Modifier,
    context: Context,
    stickNotes: List<StickyNoteDomain>,
    onNavigateToAddStickNote: (StickyNoteDomain) -> Unit,
    onDelete: (StickyNoteDomain) -> Unit,
    onUpdateStateNotificaion: (Int, Boolean) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(stickNotes, key = { it.id!! }) { stickNote ->

            val switToDismessState = rememberSwipeToDismissBoxState()

            LaunchedEffect(key1 = switToDismessState.currentValue) {
                when (switToDismessState.currentValue) {
                    SwipeToDismissBoxValue.StartToEnd -> onNavigateToAddStickNote(stickNote)
                    SwipeToDismissBoxValue.EndToStart -> onDelete(stickNote)
                    SwipeToDismissBoxValue.Settled -> {}
                }
            }

            MySwippe(
                modifier = modifier,
                onUpdateStateNotificaion = onUpdateStateNotificaion,
                stickNote = stickNote,
                context = context,
                dismissState = switToDismessState
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MySwippe(
    modifier: Modifier = Modifier,
    onUpdateStateNotificaion: (Int, Boolean) -> Unit,
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
            color = Color.Red
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
            onUpdateStateNotificaion = onUpdateStateNotificaion,
            modifier = modifier,
            context = context
        )
    }
}
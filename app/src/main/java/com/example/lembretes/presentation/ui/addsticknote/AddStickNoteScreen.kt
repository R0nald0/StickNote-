package com.example.lembretes.presentation.ui.addsticknote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lembretes.R
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteCheckBox
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickNoteTagArea
import com.example.lembretes.presentation.ui.addsticknote.widgets.StickyNoteCalendar
import com.example.lembretes.presentation.ui.shared.widgets.LoadingScreen
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteAppBar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteTextField
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.AddUpdateViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddStickNoteScreen(
    modifier: Modifier = Modifier,
    idStikcNote: String? = null,
    onClosed: () -> Unit,
) {

    val addUpdateViewModel = hiltViewModel<AddUpdateViewModel>()

    var loader by remember {
        mutableStateOf(true)
    }

    Log.i("INFO_", "AddStickNoteScreen: loader : $loader")

    if (loader) {
        LoadingScreen(isLoading = {
            if (idStikcNote != "0") {
                addUpdateViewModel.findById(idStikcNote!!.toInt())
            }
            loader = false
        })
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            val ui by addUpdateViewModel.addScreenUi.collectAsStateWithLifecycle()

            if (idStikcNote != null && idStikcNote != "0") {
                if (ui.stickyNoteDomain.name.isNotBlank()) {
                    MyScreen(
                        onSave = addUpdateViewModel::updateStickNote,
                        stickyNoteDomain = ui.stickyNoteDomain,
                        onClosed = onClosed,
                    )
                }

            } else {
                MyScreen(
                    onSave = addUpdateViewModel::insertStickNote,
                    stickyNoteDomain = null,
                    modifier = modifier,
                    onClosed = onClosed
                )
            }
        }

    }

}



@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun MyScreen(
    onSave: (StickyNoteDomain) -> Unit,
    stickyNoteDomain: StickyNoteDomain?,
    modifier: Modifier = Modifier,
    onClosed: () -> Unit
) {
    Scaffold(
        topBar = {
            StickNoteAppBar(
                onClosed = onClosed,
                title = if (stickyNoteDomain != null) stringResource(R.string.editar_lembrete)
                else stringResource(R.string.adicionar_lembrete)
            )
        }
    ) { paddingValues ->
        var isRemember by rememberSaveable { mutableStateOf(false) }
        val tags = remember { mutableStateListOf<String>() }
        var tag by remember { mutableStateOf("") }

        var selectedDate: Long? by rememberSaveable {
            mutableStateOf(
                stickyNoteDomain?.dateTime
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var lembreteName by rememberSaveable { mutableStateOf(stickyNoteDomain?.name ?: "") }

            var lembreteDescription by rememberSaveable {
                mutableStateOf(stickyNoteDomain?.description ?:"")
            }

          val isValid by  remember {
                derivedStateOf { validateField(lembreteName,lembreteDescription) }
            }

            Image(
                modifier = modifier.size(120.dp),
                painter = painterResource(id = R.drawable.agenda),
                contentDescription = "Logo aplicativo"
            )

            Spacer(modifier = modifier.height(16.dp))

            StickNoteTextField(
                maxLines = 1,
                value = lembreteName,
                label = "Título",
                isError = false,
                onChange = { value -> lembreteName = value },
                singleLine = true,
                icon = {  },
                trailingIcon = { },
                supportTexting = {}
            )

            StickNoteTextField(
                maxLines = 5,
                value = lembreteDescription,
                label = stringResource(R.string.descri_o),
                isError = false,
                onChange = { value ->
                    lembreteDescription = value
                },
                singleLine = false,
                icon = {  },
                trailingIcon ={} ,
                supportTexting = {}
            )

            Spacer(modifier.height(15.dp))

            LaunchedEffect(key1 = stickyNoteDomain?.tags?.isNotEmpty()) {
                if (stickyNoteDomain != null && stickyNoteDomain.tags.isNotEmpty()) {
                    tags.addAll(stickyNoteDomain.tags)
                }
            }

            StickNoteTagArea(
                modifier = Modifier,
                limitCha = 3,
                label = "Tag",
                tags = tags,
                tag = tag,
                onAdd = {
                    if (tag.isBlank()) return@StickNoteTagArea
                    if (tags.size <= 1) {
                        val tagFormat = tag.replaceBefore(tag.first(), "#")
                        tags.add(tagFormat)
                        tag = ""
                    }
                },
                onRemove = {
                    if (tags.size > 0) {
                        tags.remove(it)
                    }
                },
                onTextChange = {
                    if (it.length <= 3) {
                        tag = it.lowercase()
                    }
                }
            )

            Spacer(modifier.height(20.dp))

            StickyNoteCalendar(
                modifier = modifier,
                date = stickyNoteDomain?.dateTime?.let {
                    Instant.fromEpochMilliseconds(it).toLocalDateTime(
                        TimeZone.UTC).date.format(kotlinx.datetime.LocalDate.Format {
                        byUnicodePattern("dd/MM/yyyy")
                    })
                },
                onSelectedDate ={date ->
                     date?.let {
                         selectedDate = date
                     }
                }
            )

            StickNoteCheckBox(
                modifier = Modifier,
                stickyNoteDomain = stickyNoteDomain,
                isChecked = {isChecked-> isRemember = isChecked
                }
            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(
                    enabled = isValid,
                    onClick = {
                        createUpdateStickNote(
                            stickyNoteDomain,
                            lembreteName,
                            lembreteDescription,
                            selectedDate,
                            isRemember,
                            tags,
                            onSave,
                            onClosed
                        )
                    }) {
                    Text(stringResource(R.string.salvar))
                }
            }
        }
    }
}

private fun createUpdateStickNote(
    stickyNoteDomain: StickyNoteDomain?,
    lembreteName: String,
    lembreteDescription: String,
    selectedDate: Long?,
    isRemeber: Boolean,
    tags: SnapshotStateList<String>,
    onSave: (StickyNoteDomain) -> Unit,
    onClosed: () -> Unit
) {

      val stickNote = StickyNoteDomain(
            id = stickyNoteDomain?.id,
            name = lembreteName,
            description = lembreteDescription,
            dateTime = selectedDate!!,
            isRemember = isRemeber,
            tags = tags
        )
    onSave(stickNote)
    onClosed()
}

fun validateField(name: String, desciption: String): Boolean {
    return name.isNotBlank() && desciption.isNotBlank() 
}


@Preview
@Composable
private fun MyScreenPreview() {
    LembretesTheme {
        MyScreen({}, null, onClosed = {})
    }
}



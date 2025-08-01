package com.example.lembretes.presentation.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lembretes.R
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.ui.shared.widgets.LoadingScreen
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteCardView
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.SerachViewModel
import com.example.lembretes.utils.getDateCurrentSystemDefaultInLocalDateTime
import com.example.lembretes.utils.getDateFromLongOfCurrentSystemDate
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onClose :()->Unit,
    context: Context,
    onUpadteNotification:(StickyNoteDomain?, Boolean) ->Unit,
    onInfoMessage :(String) -> Unit
    ) {

    val searchViewModel = hiltViewModel<SerachViewModel>()
    var textSearch by remember { mutableStateOf("") }
    var onQueryCha by remember { mutableStateOf("") }
    var activeSearch by remember { mutableStateOf(false) }


    val uiStateSearch by searchViewModel.uiSearchState.collectAsStateWithLifecycle()

    Scaffold(

    ) {
        Box(
           modifier =  modifier
               .fillMaxSize()
               .semantics { isTraversalGroup = true },
            contentAlignment = Alignment.TopCenter
        ) {
            val colors1 = SearchBarDefaults.colors()
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = textSearch,
                        onQueryChange = {textChanged ->
                            textSearch = textChanged
                            if (textSearch.isNotEmpty()){
                                searchViewModel.findStickNotebyText(textSearch)
                             return@InputField
                            }
                        },
                        onSearch = { textToSearch ->
                            if (textToSearch.isNotEmpty()){
                                listOf("teste","test")
                                StickNoteLog.info(textToSearch)
                                searchViewModel.findStickNotebyText(textSearch)
                              return@InputField
                            }
                            Toast.makeText(context, "Por favor,digite um título ou tag válida", Toast.LENGTH_SHORT).show()


                        },
                        expanded = activeSearch,
                        onExpandedChange = {isEx->
                            activeSearch =isEx
                        },
                        placeholder = {
                            Text(text = "#tag ou titulo",
                                style = MaterialTheme.typography
                                    .labelLarge.copy(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .6f)
                                    )
                            )
                        },
                        leadingIcon = {
                            IconButton(onClick = onClose) {
                                Icon(Icons.Rounded.KeyboardArrowLeft, contentDescription ="" )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                textSearch = ""
                                activeSearch = false
                            }) {
                                Icon(Icons.Rounded.Close, contentDescription ="" )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    )
                },
                expanded = activeSearch,
                onExpandedChange = { isExpanded ->
                    activeSearch = isExpanded
                },
                modifier = Modifier
                    .fillMaxWidth(if (activeSearch) 1f else 0.9f)
                    .padding(it),

                shape = SearchBarDefaults.inputFieldShape,
                colors = colors1,
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                windowInsets = SearchBarDefaults.windowInsets,
                content = {
                    if (uiStateSearch.loading) {
                        LoadingScreen(
                            isLoading = { uiStateSearch.loading }
                        )
                    } else {
                        if (uiStateSearch.resultSearches.isEmpty() || textSearch.isEmpty()) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Digite um título ou busque por uma #tag...",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.W600
                                    )
                                )
                            }
                        }
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            items(uiStateSearch.resultSearches) { stickNote ->

                                StickNoteCardView(
                                    stickyNoteDomain = stickNote,
                                    onUpdateStateNotification = { isRemember ->
                                        if (stickNote.id == null || Clock.System.getDateFromLongOfCurrentSystemDate(stickNote.dateTime)  <=
                                            Clock.System.getDateCurrentSystemDefaultInLocalDateTime()
                                        ) {
                                            onInfoMessage(context.getString(R.string.lembrete_com_a_data_inv_lida))
                                            return@StickNoteCardView
                                        }
                                        onUpadteNotification(stickNote, isRemember)
                                    },
                                    modifier = modifier
                                )
                            }
                        }
                    }
                },
            )
        }


    }
}

@Preview
@Composable
private fun SearchScreenPrev() {
    LembretesTheme {
        SearchScreen(
            modifier =  Modifier,
            context = LocalContext.current,
            onClose ={},
            onUpadteNotification = {not,iss->},
            onInfoMessage = {info ->}
        )
    }
}
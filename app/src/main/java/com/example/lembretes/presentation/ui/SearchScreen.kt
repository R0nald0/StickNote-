package com.example.lembretes.presentation.ui

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lembretes.presentation.ui.shared.widgets.LoadingScreen
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteCardView
import com.example.lembretes.presentation.viewmodel.SerachViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onClose :()->Unit,

    context: Context,
    onUpadteNotification:(Int,Boolean) ->Unit
    ) {

    val searchViewModel = hiltViewModel<SerachViewModel>()
    var textSearch by remember { mutableStateOf("") }
    var onQueryCha by remember { mutableStateOf("") }
    var activeSearch by remember { mutableStateOf(false) }

    val uiStateSearch by searchViewModel.uiSearchState.collectAsStateWithLifecycle()

    Scaffold {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
            ,
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
            placeholder = {
                Text(text = "tag...ou titulo",
                    style = MaterialTheme.typography
                        .labelLarge.copy(
                            color = Color.DarkGray.copy(alpha = .5f)
                        )
                )
            },

            query =textSearch ,
            onQueryChange = {
                textSearch = it
                if (textSearch.isNotEmpty()){
                    searchViewModel.findStickNotebyText(textSearch)
                }
            },
            onSearch = {
                listOf("teste","test")
                Log.d("INFO_", "SearchScreen:  ${it}")
            },
            active = activeSearch ,
            onActiveChange = {
                activeSearch = it
            },
            content = {
                if (uiStateSearch.loading){
                       LoadingScreen(
                           isLoading = {uiStateSearch.loading})
                }else{
                    if (uiStateSearch.resultSearches.isEmpty() || textSearch.isEmpty()){
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Digite um título ou busque por uma #tag para proucrar...",
                                style = MaterialTheme.typography.bodyMedium
                                )
                        }
                    }
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                      items(uiStateSearch.resultSearches){ stickNote->
                          Log.d("INFO_", "onQueryChange:  ${stickNote.name}")
                          StickNoteCardView(
                              stickyNoteDomain =stickNote ,
                              onUpdateStateNotificaion = onUpadteNotification,
                              context =context  ,
                              modifier =modifier
                          )
                      }
                    }
                }
            }
        )
    }




}
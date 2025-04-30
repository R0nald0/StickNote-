package com.example.lembretes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class GlobalState(
    val isLoading : Boolean = false,
    var message : String? = null
)


@HiltViewModel
class GlobalVIewModel @Inject constructor () : ViewModel() {
   private val _globalState = MutableStateFlow<GlobalState>(GlobalState())
   var globalState : StateFlow<GlobalState> =_globalState
    fun  showLoader(){
        _globalState.update {
            it.copy(isLoading = true)
        }
    }
   fun  hideLoader(){
        if (_globalState.value.isLoading){
            _globalState.update {
                it.copy(isLoading = false)
            }
        }
    }
}
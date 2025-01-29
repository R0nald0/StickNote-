package com.example.lembretes.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.domain.model.User
import com.example.lembretes.domain.usecase.user.CreateUser
import com.example.lembretes.domain.usecase.user.FindUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val findUser: FindUser,
    private val createUser: CreateUser,
):ViewModel() {
     private val _user = MutableStateFlow(User())
      var user : StateFlow<User> =_user.asStateFlow()

    fun crateUser(name :String ,urlPerfilPhoto : String){
        viewModelScope.launch{
            runCatching {
                val user = User(id = 1, name = name, photoProfile = "")
                createUser.createUser(user)
            }.fold(
                onSuccess = {
                    Log.i("INFO_", "crateUser: usuario criado com o usuario")
                },
                onFailure = {
                    Log.i("INFO_", "erroe: erro ao cadastrar usuario ${it.message} ${it.stackTrace}")
                }
            )
        }
    }
    fun findFirstUser(){
        viewModelScope.launch {
            findUser.findFistUser().collect { user ->
                if (user.id != 0 ){
                    _user.value = user
                }
            }
        }
    }

}
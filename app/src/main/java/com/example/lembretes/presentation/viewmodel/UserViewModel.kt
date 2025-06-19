package com.example.lembretes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lembretes.core.Constants
import com.example.lembretes.data.repository.PreferenceRepository
import com.example.lembretes.domain.model.User
import com.example.lembretes.domain.model.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val preferencesRepository: PreferenceRepository
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    var user: StateFlow<User> = _user.asStateFlow()

    init {
        findFirstUser()
    }

    fun crateUser(name: String, urlPerfilPhoto: String, message: (String) -> Unit) {
        viewModelScope.launch {
            runCatching {
                val user = User(id = 1, name = name, photoProfile = urlPerfilPhoto)
                preferencesRepository.savePreference(user.toJson(), Constants.USER_KEY)
            }.fold(
                onSuccess = {
                    message("Atualizado dados do usuario")
                },
                onFailure = {
                    message("Erro ao cadastrar usuário")
                }
            )
        }
    }

    fun findFirstUser() {
        viewModelScope.launch {
            preferencesRepository.readKey(Constants.USER_KEY)
                .catch {

                }
                .collect { preferences ->
                    val userString = preferences.get<String>(Constants.USER_KEY)
                    if (userString == null) return@collect

                    val user = User.fromJson(json = userString)
                    if (user == null) return@collect

                    _user.value = user
                }
        }
    }

}
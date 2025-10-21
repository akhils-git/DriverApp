package com.girfalco.driverapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girfalco.driverapp.repository.LoginRepository
import com.girfalco.driverapp.network.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val response: LoginResponse) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val state: StateFlow<LoginUiState> = _state

    fun login(email: String, password: String, mobile: String, fcmToken: String) {
        _state.value = LoginUiState.Loading
        viewModelScope.launch {
            val result = repo.login(email, password, mobile, fcmToken)
            result.fold(
                onSuccess = { resp -> _state.value = LoginUiState.Success(resp) },
                onFailure = { err -> _state.value = LoginUiState.Error(err.message ?: "Unknown error") }
            )
        }
    }
}

package com.example.betterspend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterspend.data.model.GenericResponse
import com.example.betterspend.data.model.LoginResponse
import com.example.betterspend.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewmodel() : ViewModel() {
    private val repository = UserRepository()
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)

    val loginState: StateFlow<LoginState> get() = _loginState
    val registerState: StateFlow<RegisterState> get() = _registerState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = repository.login(email, password)
                if (response.success) {
                    _loginState.value = LoginState.Success(response)
                } else {
                    _loginState.value = LoginState.Error(response.message)
                }

            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun register(email : String, password : String, confirmPassword : String) {
        viewModelScope.launch{
            _registerState.value = RegisterState.Loading
            try {
                val response = repository.register(email, password, confirmPassword)
                if (response.success) {
                    _registerState.value = RegisterState.Success(response)
                } else {
                    _registerState.value = RegisterState.Error(response.message)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val response: GenericResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
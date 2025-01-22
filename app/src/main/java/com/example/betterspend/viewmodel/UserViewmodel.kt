package com.example.betterspend.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterspend.data.model.GenericResponse
import com.example.betterspend.data.model.LoginResponse
import com.example.betterspend.data.repository.UserRepository
import com.example.betterspend.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                    // Save user ID for future uses in the app and for "already logged in" feature
                    SharedPrefManager.saveLoginState(response.id.toString())

                    // Save profile picture URI for future uses in the app
                    SharedPrefManager.saveProfilePictureUri(response.image)

                    // Update the state with the success response
                    _loginState.value = LoginState.Success(response)
                } else {
                    _loginState.value = LoginState.Error(response.message)
                }

            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun register(email : String, password : String, confirmPassword : String, imageUri : Uri?) {
        viewModelScope.launch{
            _registerState.value = RegisterState.Loading
            try {
                val response = repository.register(email, password, confirmPassword, imageUri)
                if (response.success) {
                    SharedPrefManager.saveProfilePictureUri(imageUri)
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
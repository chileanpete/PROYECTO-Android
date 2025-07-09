package com.example.proyecto_droid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.User
import com.example.proyecto_droid.data.repository.UnifiedUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false,
    val currentUser: User? = null
)

sealed class LoginEvent {
    data class UpdateEmail(val email: String) : LoginEvent()
    data class UpdatePassword(val password: String) : LoginEvent()
    object TogglePasswordVisibility : LoginEvent()
    object Login : LoginEvent()
    object ClearError : LoginEvent()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository = UnifiedUserRepository(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UpdateEmail -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }
            is LoginEvent.UpdatePassword -> {
                _uiState.value = _uiState.value.copy(password = event.password)
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    passwordVisible = !_uiState.value.passwordVisible
                )
            }
            is LoginEvent.Login -> {
                loginUser()
            }
            is LoginEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    private fun loginUser() {
        val state = _uiState.value
        
        // Validación básica
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            
            try {
                val result = userRepository.loginUser(state.email, state.password)
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = state.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            currentUser = user,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = state.copy(
                            isLoading = false,
                            error = exception.message ?: "Error en el login"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    error = e.message ?: "Error inesperado"
                )
            }
        }
    }

    fun resetLoginState() {
        _uiState.value = LoginUiState()
    }
} 
package com.example.proyecto_droid.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de login
 * Usa casos de uso del dominio para manejar la lógica de negocio
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UpdateEmail -> {
                _uiState.value = _uiState.value.copy(
                    email = event.email,
                    error = null // Limpiar error al escribir
                )
            }
            is LoginUiEvent.UpdatePassword -> {
                _uiState.value = _uiState.value.copy(
                    password = event.password,
                    error = null // Limpiar error al escribir
                )
            }
            is LoginUiEvent.TogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    passwordVisible = !_uiState.value.passwordVisible
                )
            }
            is LoginUiEvent.Login -> {
                performLogin()
            }
            is LoginUiEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
            is LoginUiEvent.NavigateToRegister -> {
                // Evento para navegación - manejado por la UI
            }
            is LoginUiEvent.NavigateToForgotPassword -> {
                // Evento para navegación - manejado por la UI
            }
        }
    }

    private fun performLogin() {
        val currentState = _uiState.value
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val result = loginUseCase(currentState.email, currentState.password)
                
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            currentUser = user,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = exception.message ?: "Error desconocido durante el login"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
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
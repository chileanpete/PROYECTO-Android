package com.example.proyecto_droid.presentation.login

import com.example.proyecto_droid.domain.model.User

/**
 * Estado de UI para la pantalla de login
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false,
    val currentUser: User? = null
)

/**
 * Eventos de UI para la pantalla de login
 */
sealed class LoginUiEvent {
    data class UpdateEmail(val email: String) : LoginUiEvent()
    data class UpdatePassword(val password: String) : LoginUiEvent()
    object TogglePasswordVisibility : LoginUiEvent()
    object Login : LoginUiEvent()
    object ClearError : LoginUiEvent()
    object NavigateToRegister : LoginUiEvent()
    object NavigateToForgotPassword : LoginUiEvent()
} 
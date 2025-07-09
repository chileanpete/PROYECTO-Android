package com.example.proyecto_droid.presentation.session

import com.example.proyecto_droid.domain.model.User

/**
 * Estado de UI para el manejo de sesión global
 */
data class SessionUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    val currentUser: User? = null,
    val error: String? = null
)

/**
 * Eventos de UI para el manejo de sesión
 */
sealed class SessionUiEvent {
    data class Login(val user: User) : SessionUiEvent()
    object Logout : SessionUiEvent()
    object CheckSession : SessionUiEvent()
    object ClearError : SessionUiEvent()
} 
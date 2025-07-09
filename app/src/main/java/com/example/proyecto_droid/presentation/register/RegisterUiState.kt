package com.example.proyecto_droid.presentation.register

import com.example.proyecto_droid.domain.model.User

/**
 * Estado de UI para la pantalla de registro
 */
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val altura: String = "",
    val peso: String = "",
    val nivelActividad: String = "",
    val objetivoPrincipal: String = "",
    val preferenciasAlimentarias: String = "",
    val alergias: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false,
    val registeredUser: User? = null
)

/**
 * Eventos de UI para la pantalla de registro
 */
sealed class RegisterUiEvent {
    data class UpdateEmail(val email: String) : RegisterUiEvent()
    data class UpdatePassword(val password: String) : RegisterUiEvent()
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterUiEvent()
    data class UpdateNombre(val nombre: String) : RegisterUiEvent()
    data class UpdateApellidos(val apellidos: String) : RegisterUiEvent()
    data class UpdateFechaNacimiento(val fecha: String) : RegisterUiEvent()
    data class UpdateGenero(val genero: String) : RegisterUiEvent()
    data class UpdateAltura(val altura: String) : RegisterUiEvent()
    data class UpdatePeso(val peso: String) : RegisterUiEvent()
    data class UpdateNivelActividad(val nivel: String) : RegisterUiEvent()
    data class UpdateObjetivoPrincipal(val objetivo: String) : RegisterUiEvent()
    data class UpdatePreferenciasAlimentarias(val preferencias: String) : RegisterUiEvent()
    data class UpdateAlergias(val alergias: String) : RegisterUiEvent()
    object TogglePasswordVisibility : RegisterUiEvent()
    object ToggleConfirmPasswordVisibility : RegisterUiEvent()
    object Register : RegisterUiEvent()
    object ClearError : RegisterUiEvent()
    object NavigateToLogin : RegisterUiEvent()
} 
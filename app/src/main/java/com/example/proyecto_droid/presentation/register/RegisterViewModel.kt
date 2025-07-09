package com.example.proyecto_droid.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.domain.model.User
import com.example.proyecto_droid.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de registro
 * Usa casos de uso del dominio para manejar la lógica de negocio
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.UpdateEmail -> {
                _uiState.value = _uiState.value.copy(
                    email = event.email,
                    error = null
                )
            }
            is RegisterUiEvent.UpdatePassword -> {
                _uiState.value = _uiState.value.copy(
                    password = event.password,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateConfirmPassword -> {
                _uiState.value = _uiState.value.copy(
                    confirmPassword = event.confirmPassword,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateNombre -> {
                _uiState.value = _uiState.value.copy(
                    nombre = event.nombre,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateApellidos -> {
                _uiState.value = _uiState.value.copy(
                    apellidos = event.apellidos,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateFechaNacimiento -> {
                _uiState.value = _uiState.value.copy(
                    fechaNacimiento = event.fecha,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateGenero -> {
                _uiState.value = _uiState.value.copy(
                    genero = event.genero,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateAltura -> {
                _uiState.value = _uiState.value.copy(
                    altura = event.altura,
                    error = null
                )
            }
            is RegisterUiEvent.UpdatePeso -> {
                _uiState.value = _uiState.value.copy(
                    peso = event.peso,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateNivelActividad -> {
                _uiState.value = _uiState.value.copy(
                    nivelActividad = event.nivel,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateObjetivoPrincipal -> {
                _uiState.value = _uiState.value.copy(
                    objetivoPrincipal = event.objetivo,
                    error = null
                )
            }
            is RegisterUiEvent.UpdatePreferenciasAlimentarias -> {
                _uiState.value = _uiState.value.copy(
                    preferenciasAlimentarias = event.preferencias,
                    error = null
                )
            }
            is RegisterUiEvent.UpdateAlergias -> {
                _uiState.value = _uiState.value.copy(
                    alergias = event.alergias,
                    error = null
                )
            }
            is RegisterUiEvent.TogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    passwordVisible = !_uiState.value.passwordVisible
                )
            }
            is RegisterUiEvent.ToggleConfirmPasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    confirmPasswordVisible = !_uiState.value.confirmPasswordVisible
                )
            }
            is RegisterUiEvent.Register -> {
                performRegister()
            }
            is RegisterUiEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
            is RegisterUiEvent.NavigateToLogin -> {
                // Evento para navegación - manejado por la UI
            }
        }
    }

    private fun performRegister() {
        val currentState = _uiState.value
        
        // Validación de contraseñas
        if (currentState.password != currentState.confirmPassword) {
            _uiState.value = currentState.copy(
                error = "Las contraseñas no coinciden"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val user = User(
                    email = currentState.email,
                    nombre = currentState.nombre,
                    apellidos = currentState.apellidos,
                    fechaNacimiento = currentState.fechaNacimiento,
                    genero = currentState.genero,
                    alturaCm = currentState.altura.toIntOrNull() ?: 0,
                    pesoKg = currentState.peso.toDoubleOrNull() ?: 0.0,
                    nivelActividad = currentState.nivelActividad,
                    objetivoPrincipal = currentState.objetivoPrincipal,
                    preferenciasAlimentarias = currentState.preferenciasAlimentarias,
                    alergias = currentState.alergias
                )
                
                val result = registerUseCase(user, currentState.password)
                
                result.fold(
                    onSuccess = { registeredUser ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            isRegistrationSuccessful = true,
                            registeredUser = registeredUser,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = exception.message ?: "Error desconocido durante el registro"
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

    fun resetRegisterState() {
        _uiState.value = RegisterUiState()
    }
} 
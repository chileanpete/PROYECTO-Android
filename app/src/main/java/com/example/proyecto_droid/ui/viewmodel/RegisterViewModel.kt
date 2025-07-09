package com.example.proyecto_droid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.User
import com.example.proyecto_droid.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

data class RegisterUiState(
    val currentStep: Int = 1,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val alturaCm: String = "",
    val pesoKg: String = "",
    val nivelActividad: String = "",
    val objetivoPrincipal: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistrationSuccessful: Boolean = false,
    val nivelesActividad: List<String> = emptyList(),
    val objetivos: List<String> = emptyList(),
    val generos: List<String> = emptyList(),
    val alturas: List<String> = (100..250).map { "${it} cm" },
    val pesos: List<String> = (30..200).map { "${it} kg" }
)

sealed class RegisterEvent {
    data class UpdateEmail(val email: String) : RegisterEvent()
    data class UpdatePassword(val password: String) : RegisterEvent()
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterEvent()
    data class UpdateNombre(val nombre: String) : RegisterEvent()
    data class UpdateApellidos(val apellidos: String) : RegisterEvent()
    data class UpdateFechaNacimiento(val fecha: String) : RegisterEvent()
    data class UpdateGenero(val genero: String) : RegisterEvent()
    data class UpdateAltura(val altura: String) : RegisterEvent()
    data class UpdatePeso(val peso: String) : RegisterEvent()
    data class UpdateNivelActividad(val nivel: String) : RegisterEvent()
    data class UpdateObjetivoPrincipal(val objetivo: String) : RegisterEvent()
    object NextStep : RegisterEvent()
    object PreviousStep : RegisterEvent()
    object RegisterUser : RegisterEvent()
    object ClearError : RegisterEvent()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository = UserRepository(application)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        loadDropdownData()
    }

    private fun loadDropdownData() {
        _uiState.value = _uiState.value.copy(
            nivelesActividad = userRepository.getNivelesActividad(),
            objetivos = userRepository.getObjetivos(),
            generos = userRepository.getGeneros()
        )
    }

    fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UpdateEmail -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }
            is RegisterEvent.UpdatePassword -> {
                _uiState.value = _uiState.value.copy(password = event.password)
            }
            is RegisterEvent.UpdateConfirmPassword -> {
                _uiState.value = _uiState.value.copy(confirmPassword = event.confirmPassword)
            }
            is RegisterEvent.UpdateNombre -> {
                _uiState.value = _uiState.value.copy(nombre = event.nombre)
            }
            is RegisterEvent.UpdateApellidos -> {
                _uiState.value = _uiState.value.copy(apellidos = event.apellidos)
            }
            is RegisterEvent.UpdateFechaNacimiento -> {
                _uiState.value = _uiState.value.copy(fechaNacimiento = event.fecha)
            }
            is RegisterEvent.UpdateGenero -> {
                _uiState.value = _uiState.value.copy(genero = event.genero)
            }
            is RegisterEvent.UpdateAltura -> {
                _uiState.value = _uiState.value.copy(alturaCm = event.altura)
            }
            is RegisterEvent.UpdatePeso -> {
                _uiState.value = _uiState.value.copy(pesoKg = event.peso)
            }
            is RegisterEvent.UpdateNivelActividad -> {
                _uiState.value = _uiState.value.copy(nivelActividad = event.nivel)
            }
            is RegisterEvent.UpdateObjetivoPrincipal -> {
                _uiState.value = _uiState.value.copy(objetivoPrincipal = event.objetivo)
            }
            is RegisterEvent.NextStep -> {
                if (validateCurrentStep()) {
                    _uiState.value = _uiState.value.copy(
                        currentStep = _uiState.value.currentStep + 1,
                        error = null
                    )
                }
            }
            is RegisterEvent.PreviousStep -> {
                _uiState.value = _uiState.value.copy(
                    currentStep = (_uiState.value.currentStep - 1).coerceAtLeast(1),
                    error = null
                )
            }
            is RegisterEvent.RegisterUser -> {
                registerUser()
            }
            is RegisterEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    private fun validateCurrentStep(): Boolean {
        val currentState = _uiState.value
        val error = when (currentState.currentStep) {
            1 -> validateStep1()
            2 -> validateStep2()
            3 -> validateStep3()
            else -> null
        }
        
        if (error != null) {
            _uiState.value = currentState.copy(error = error)
            return false
        }
        return true
    }

    private fun validateStep1(): String? {
        val state = _uiState.value
        return when {
            state.email.isBlank() -> "El correo electrónico es obligatorio"
            !isValidEmail(state.email) -> "Formato de correo electrónico inválido"
            state.nombre.isBlank() -> "El nombre es obligatorio"
            state.apellidos.isBlank() -> "Los apellidos son obligatorios"
            state.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            state.password != state.confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    private fun validateStep2(): String? {
        val state = _uiState.value
        return when {
            state.fechaNacimiento.isBlank() -> "La fecha de nacimiento es obligatoria"
            state.genero.isBlank() -> "El género es obligatorio"
            state.alturaCm.isBlank() -> "La altura es obligatoria"
            state.pesoKg.isBlank() -> "El peso es obligatorio"
            else -> null
        }
    }

    private fun validateStep3(): String? {
        val state = _uiState.value
        return when {
            state.nivelActividad.isBlank() -> "El nivel de actividad física es obligatorio"
            state.objetivoPrincipal.isBlank() -> "El objetivo principal es obligatorio"
            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun registerUser() {
        val state = _uiState.value
        
        if (!validateCurrentStep()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            
            try {
                val user = User(
                    email = state.email,
                    passwordHash = hashPassword(state.password),
                    nombre = state.nombre,
                    apellidos = state.apellidos,
                    fechaNacimiento = state.fechaNacimiento,
                    genero = state.genero,
                    alturaCm = extractNumber(state.alturaCm),
                    pesoKg = extractNumber(state.pesoKg).toDouble(),
                    nivelActividad = state.nivelActividad,
                    objetivoPrincipal = state.objetivoPrincipal,
                    preferenciasAlimentarias = null, // Se configurará después en el perfil
                    alergias = null // Se configurará después en el perfil
                )

                val result = userRepository.registerUser(user)
                result.fold(
                    onSuccess = { registeredUser ->
                        _uiState.value = state.copy(
                            isLoading = false,
                            isRegistrationSuccessful = true,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = state.copy(
                            isLoading = false,
                            error = exception.message ?: "Error en el registro"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    error = e.message ?: "Error inesperado durante el registro"
                )
            }
        }
    }
    
    // Función auxiliar para extraer números de strings como "175 cm" o "70 kg"
    fun extractNumber(value: String): Int {
        return value.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
} 
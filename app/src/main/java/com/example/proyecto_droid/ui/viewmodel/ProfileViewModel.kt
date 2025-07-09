package com.example.proyecto_droid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.User
import com.example.proyecto_droid.data.repository.UnifiedUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val shouldNavigateToEditProfile: Boolean = false,
    val shouldNavigateToEditPreferences: Boolean = false,
    val shouldNavigateToChangePassword: Boolean = false,
    val updateSuccess: Boolean = false,
    val successMessage: String? = null
)

sealed class ProfileEvent {
    object LoadProfile : ProfileEvent()
    object RefreshProfile : ProfileEvent()
    data class UpdateProfile(val user: User) : ProfileEvent()
    object Logout : ProfileEvent()
    object NavigateToEditProfile : ProfileEvent()
    object NavigateToEditPreferences : ProfileEvent()
    object NavigateToChangePassword : ProfileEvent()
    data class ChangePassword(val currentPassword: String, val newPassword: String) : ProfileEvent()
    object ClearSuccess : ProfileEvent()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository = UnifiedUserRepository(application)
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadProfile()
    }
    
    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadProfile -> {
                loadProfile()
            }
            is ProfileEvent.RefreshProfile -> {
                refreshProfile()
            }
            is ProfileEvent.UpdateProfile -> {
                updateProfile(event.user)
            }
            is ProfileEvent.Logout -> {
                logout()
            }
            is ProfileEvent.NavigateToEditProfile -> {
                _uiState.value = _uiState.value.copy(shouldNavigateToEditProfile = true)
            }
            is ProfileEvent.NavigateToEditPreferences -> {
                _uiState.value = _uiState.value.copy(shouldNavigateToEditPreferences = true)
            }
            is ProfileEvent.NavigateToChangePassword -> {
                _uiState.value = _uiState.value.copy(shouldNavigateToChangePassword = true)
            }
            is ProfileEvent.ChangePassword -> {
                changePassword(event.currentPassword, event.newPassword)
            }
            is ProfileEvent.ClearSuccess -> {
                _uiState.value = _uiState.value.copy(
                    updateSuccess = false,
                    successMessage = null,
                    error = null
                )
            }
        }
    }
    
    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Verificar si hay una sesión activa (usando first() en lugar de collect)
                val isLoggedIn = userRepository.isUserLoggedIn().first()
                
                if (isLoggedIn) {
                    // Obtener el perfil del usuario desde la API
                    val result = userRepository.getCurrentUserProfile()
                    
                    if (result.isSuccess) {
                        _uiState.value = _uiState.value.copy(
                            currentUser = result.getOrNull(),
                            isLoading = false,
                            error = null,
                            isLoggedIn = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            currentUser = null,
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Error al cargar perfil",
                            isLoggedIn = true
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        currentUser = null,
                        isLoading = false,
                        error = null, // No mostrar error cuando no hay sesión
                        isLoggedIn = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    currentUser = null,
                    isLoading = false,
                    error = "Error inesperado: ${e.message}",
                    isLoggedIn = false
                )
            }
        }
    }
    
    private fun refreshProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val result = userRepository.getCurrentUserProfile()
                
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        currentUser = result.getOrNull(),
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al actualizar perfil"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }
    
    private fun updateProfile(user: User) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null, 
                updateSuccess = false, 
                successMessage = null
            )
            
            try {
                val result = userRepository.updateUser(user)
                
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        currentUser = result.getOrNull(),
                        isLoading = false,
                        error = null,
                        updateSuccess = true,
                        successMessage = "Perfil actualizado exitosamente"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al actualizar perfil",
                        updateSuccess = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}",
                    updateSuccess = false
                )
            }
        }
    }
    
    private fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logoutUser()
                _uiState.value = ProfileUiState(isLoading = false, isLoggedIn = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cerrar sesión: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    fun clearNavigationState() {
        _uiState.value = _uiState.value.copy(
            shouldNavigateToEditProfile = false,
            shouldNavigateToEditPreferences = false,
            shouldNavigateToChangePassword = false,
            updateSuccess = false,
            successMessage = null,
            error = null
        )
    }
    
    private fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Validar que la contraseña actual no esté vacía
                if (currentPassword.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "La contraseña actual es requerida"
                    )
                    return@launch
                }
                
                // Validar que la nueva contraseña tenga al menos 6 caracteres
                if (newPassword.length < 6) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "La nueva contraseña debe tener al menos 6 caracteres"
                    )
                    return@launch
                }
                
                // Llamar al repositorio para cambiar la contraseña
                val result = userRepository.changePassword(currentPassword, newPassword)
                
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al cambiar contraseña"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }
} 
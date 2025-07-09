package com.example.proyecto_droid.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.domain.usecase.auth.LogoutUseCase
import com.example.proyecto_droid.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el manejo de sesión global
 * Maneja el estado de autenticación a nivel de aplicación
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun onEvent(event: SessionUiEvent) {
        when (event) {
            is SessionUiEvent.Login -> {
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = true,
                    currentUser = event.user,
                    isLoading = false,
                    error = null
                )
            }
            is SessionUiEvent.Logout -> {
                performLogout()
            }
            is SessionUiEvent.CheckSession -> {
                checkSession()
            }
            is SessionUiEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(error = null)
            }
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                // Combinar flujos para verificar el estado de sesión
                combine(
                    userRepository.isUserLoggedIn(),
                    userRepository.getCurrentUserEmail(),
                    userRepository.getCurrentUserName()
                ) { isLoggedIn, email, name ->
                    Triple(isLoggedIn, email, name)
                }.collect { (isLoggedIn, email, name) ->
                    if (isLoggedIn && email != null) {
                        // Si está logueado, intentar obtener el perfil completo
                        val profileResult = userRepository.getCurrentUserProfile()
                        profileResult.fold(
                            onSuccess = { user ->
                                _uiState.value = _uiState.value.copy(
                                    isLoggedIn = true,
                                    currentUser = user,
                                    isLoading = false,
                                    error = null
                                )
                            },
                            onFailure = { exception ->
                                // Si falla obtener el perfil, usar datos básicos
                                _uiState.value = _uiState.value.copy(
                                    isLoggedIn = true,
                                    currentUser = null,
                                    isLoading = false,
                                    error = exception.message
                                )
                            }
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoggedIn = false,
                            currentUser = null,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = false,
                    currentUser = null,
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                logoutUseCase()
                
                _uiState.value = SessionUiState(
                    isLoggedIn = false,
                    isLoading = false,
                    currentUser = null,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error durante el logout"
                )
            }
        }
    }

    fun getCurrentUser() = _uiState.value.currentUser
    fun isLoggedIn() = _uiState.value.isLoggedIn
} 
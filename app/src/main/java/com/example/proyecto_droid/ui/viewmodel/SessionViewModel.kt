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

data class SessionUiState(
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val userEmail: String? = null,
    val userName: String? = null,
    val isLoading: Boolean = true
)

sealed class SessionEvent {
    object CheckSession : SessionEvent()
    object Logout : SessionEvent()
    data class Login(val user: User) : SessionEvent()
}

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userRepository = UserRepository(application)
    
    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()
    
    init {
        checkSession()
    }
    
    fun handleEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.CheckSession -> {
                checkSession()
            }
            is SessionEvent.Logout -> {
                logout()
            }
            is SessionEvent.Login -> {
                login(event.user)
            }
        }
    }
    
    private fun checkSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Verificar si hay una sesión activa
                val isLoggedIn = userRepository.isUserLoggedIn()
                val userEmail = userRepository.getCurrentUserEmail()
                val userName = userRepository.getCurrentUserName()
                
                // Combinar los flujos
                isLoggedIn.collect { loggedIn ->
                    userEmail.collect { email ->
                        userName.collect { name ->
                            val currentUser = if (loggedIn) {
                                // Intentar obtener el perfil del usuario desde la API
                                try {
                                    userRepository.getCurrentUserProfile().getOrNull()
                                } catch (e: Exception) {
                                    null
                                }
                            } else null
                            
                            _uiState.value = SessionUiState(
                                isLoggedIn = loggedIn,
                                currentUser = currentUser,
                                userEmail = email,
                                userName = name,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = false,
                    isLoading = false
                )
            }
        }
    }
    
    private fun login(user: User) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoggedIn = true,
                currentUser = user,
                userEmail = user.email,
                userName = "${user.nombre} ${user.apellidos}",
                isLoading = false
            )
        }
    }
    
    private fun logout() {
        viewModelScope.launch {
            userRepository.logoutUser()
            _uiState.value = SessionUiState(isLoading = false)
        }
    }
} 
package com.example.proyecto_droid.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.TokenManager
import com.example.proyecto_droid.data.remote.services.AuthService
import com.example.proyecto_droid.model.LoginRequest
import kotlinx.coroutines.launch

sealed class AuthUiState{
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class loggedIn(var logged: Boolean) : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
class AuthViewModel(
    private val authService: AuthService,
    private val context: Context
) : ViewModel() {

    var authState: com.example.proyecto_droid.ui.screens.auth.AuthUiState by mutableStateOf(com.example.proyecto_droid.ui.screens.auth.AuthUiState.Idle)
        private set

    init {
        isLoggedIn()
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = com.example.proyecto_droid.ui.screens.auth.AuthUiState.Loading
            try {
                val response = authService.login(LoginRequest(email, password))
                TokenManager.saveToken(context, response.token)
                authState = com.example.proyecto_droid.ui.screens.auth.AuthUiState.Success(response.token)
                Log.e("test", "login: test", )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed: ${e.message}", e)
                authState = com.example.proyecto_droid.ui.screens.auth.AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authService.logout()
                TokenManager.clearToken(context)
                authState = com.example.proyecto_droid.ui.screens.auth.AuthUiState.Idle
            } catch (_: Exception) {}
        }
    }

    private fun isLoggedIn() {
        viewModelScope.launch {
            val response = authService.getUser();
            authState = com.example.proyecto_droid.ui.screens.auth.AuthUiState.loggedIn(true)
        }
    }
}

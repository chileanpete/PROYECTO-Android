package com.example.proyecto_droid.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.TokenManager
//import com.example.proyecto_droid.data.remote.services.AuthService
//import com.example.proyecto_droid.model.LoginRequest
import kotlinx.coroutines.launch

sealed class AuthUiState{
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class loggedIn(var logged: Boolean) : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

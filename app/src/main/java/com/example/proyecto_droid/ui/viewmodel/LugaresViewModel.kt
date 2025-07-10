package com.example.proyecto_droid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.LugarComida
import com.example.proyecto_droid.data.network.UnifiedApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados UI para la pantalla de lugares
 */
sealed class LugaresUiState {
    data object Loading : LugaresUiState()
    data object Idle : LugaresUiState()
    data class Success(val lugares: List<LugarComida>) : LugaresUiState()
    data class Error(val message: String) : LugaresUiState()
}

/**
 * ViewModel para la gestión de lugares de comida
 */
class LugaresViewModel(
    private val apiService: UnifiedApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<LugaresUiState>(LugaresUiState.Idle)
    val uiState: StateFlow<LugaresUiState> = _uiState.asStateFlow()

    init {
        loadLugares()
    }

    /**
     * Carga la lista de lugares desde la API
     */
    fun loadLugares() {
        viewModelScope.launch {
            _uiState.value = LugaresUiState.Loading
            try {
                val response = apiService.getLugares()
                if (response.success) {
                    _uiState.value = LugaresUiState.Success(response.data ?: emptyList())
                } else {
                    _uiState.value = LugaresUiState.Error(response.message ?: "Error al cargar lugares")
                }
            } catch (e: Exception) {
                _uiState.value = LugaresUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Refresca la lista de lugares
     */
    fun refreshLugares() {
        loadLugares()
    }

    /**
     * Reinicia el estado
     */
    fun resetState() {
        _uiState.value = LugaresUiState.Idle
    }
} 
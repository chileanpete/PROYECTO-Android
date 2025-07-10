package com.example.proyecto_droid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.Plato
import com.example.proyecto_droid.data.network.UnifiedApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados UI para la pantalla de platos
 */
sealed class PlatosUiState {
    data object Loading : PlatosUiState()
    data object Idle : PlatosUiState()
    data class Success(val platos: List<Plato>) : PlatosUiState()
    data class Error(val message: String) : PlatosUiState()
}

/**
 * ViewModel para la gestión de platos
 */
class PlatosViewModel(
    private val apiService: UnifiedApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlatosUiState>(PlatosUiState.Idle)
    val uiState: StateFlow<PlatosUiState> = _uiState.asStateFlow()

    // Removido init para no cargar automáticamente todos los platos

    /**
     * Carga la lista de platos desde la API
     */
    fun loadPlatos() {
        viewModelScope.launch {
            _uiState.value = PlatosUiState.Loading
            try {
                val response = apiService.getPlatosSimple()
                if (response.success) {
                    _uiState.value = PlatosUiState.Success(response.data?.data ?: emptyList())
                } else {
                    _uiState.value = PlatosUiState.Error(response.message ?: "Error al cargar platos")
                }
            } catch (e: Exception) {
                _uiState.value = PlatosUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Refresca la lista de platos
     */
    fun refreshPlatos() {
        loadPlatos()
    }

    /**
     * Busca platos por término de búsqueda
     */
    fun searchPlatos(query: String) {
        if (query.isBlank()) {
            loadPlatos()
            return
        }

        viewModelScope.launch {
            _uiState.value = PlatosUiState.Loading
            try {
                val response = apiService.searchPlatos(query)
                if (response.success) {
                    _uiState.value = PlatosUiState.Success(response.data ?: emptyList())
                } else {
                    _uiState.value = PlatosUiState.Error(response.message ?: "Error en la búsqueda")
                }
            } catch (e: Exception) {
                _uiState.value = PlatosUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Obtiene platos por categoría
     */
    fun getPlatosByCategory(categoryId: Int) {
        viewModelScope.launch {
            _uiState.value = PlatosUiState.Loading
            try {
                val response = apiService.getPlatosByCategory(categoryId)
                if (response.success) {
                    _uiState.value = PlatosUiState.Success(response.data ?: emptyList())
                } else {
                    _uiState.value = PlatosUiState.Error(response.message ?: "Error al filtrar por categoría")
                }
            } catch (e: Exception) {
                _uiState.value = PlatosUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Obtiene platos por lugar
     */
    fun getPlatosByPlace(placeId: Int) {
        viewModelScope.launch {
            _uiState.value = PlatosUiState.Loading
            try {
                val response = apiService.getPlatosByPlace(placeId)
                if (response.success) {
                    _uiState.value = PlatosUiState.Success(response.data?.data ?: emptyList())
                } else {
                    _uiState.value = PlatosUiState.Error(response.message ?: "Error al filtrar por lugar")
                }
            } catch (e: Exception) {
                _uiState.value = PlatosUiState.Error(e.message ?: "Error de conexión")
            }
        }
    }

    /**
     * Reinicia el estado
     */
    fun resetState() {
        _uiState.value = PlatosUiState.Idle
    }
} 
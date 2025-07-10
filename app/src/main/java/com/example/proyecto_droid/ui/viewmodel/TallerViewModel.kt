package com.example.proyecto_droid.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.TallerRecreativo
import com.example.proyecto_droid.data.network.UnifiedApiService
import com.example.proyecto_droid.data.repository.UnifiedUserRepository
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class TallerUiState(
    val isLoading: Boolean = true,
    val talleres: List<TallerRecreativo> = emptyList(),
    val errorMessage: String? = null,
    val hasError: Boolean = false
)

sealed class TallerEvent {
    object LoadTalleres : TallerEvent()
    object RefreshTalleres : TallerEvent()
    data class LoadTalleresByDate(val fecha: String) : TallerEvent()
    data class LoadTalleresByType(val tipo: String) : TallerEvent()
    data class LoadTalleresActivos(val userId: Int) : TallerEvent()
}

class TallerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val apiService: UnifiedApiService by lazy {
        UnifiedRetrofitClient.getApiService(application)
    }
    
    private val userRepository = UnifiedUserRepository(application)
    
    private val _uiState = MutableStateFlow(TallerUiState())
    val uiState: StateFlow<TallerUiState> = _uiState.asStateFlow()
    
    private val _talleres = MutableStateFlow<List<TallerRecreativo>>(emptyList())
    val talleres: StateFlow<List<TallerRecreativo>> = _talleres.asStateFlow()
    
    init {
        handleEvent(TallerEvent.LoadTalleres)
    }
    
    fun handleEvent(event: TallerEvent) {
        when (event) {
            is TallerEvent.LoadTalleres -> {
                loadTalleres()
            }
            is TallerEvent.RefreshTalleres -> {
                refreshTalleres()
            }
            is TallerEvent.LoadTalleresByDate -> {
                loadTalleresByDate(event.fecha)
            }
            is TallerEvent.LoadTalleresByType -> {
                loadTalleresByType(event.tipo)
            }
            is TallerEvent.LoadTalleresActivos -> {
                loadTalleresActivos(event.userId)
            }
        }
    }
    
    private fun loadTalleres() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, hasError = false)
            
            try {
                val response = apiService.getTalleresActivos()
                
                if (response.isSuccessful() && response.data != null) {
                    val talleresList = response.data
                    _talleres.value = talleresList
                    _uiState.value = _uiState.value.copy(
                        talleres = talleresList,
                        isLoading = false,
                        hasError = false,
                        errorMessage = null
                    )
                    Log.d("TallerViewModel", "Talleres cargados exitosamente: ${talleresList.size}")
                } else {
                    val errorMsg = response.getErrorMessage()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage = errorMsg
                    )
                    Log.e("TallerViewModel", "Error en respuesta: $errorMsg")
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("TallerViewModel", "Error de sintaxis JSON", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error de formato en la respuesta del servidor. Intenta de nuevo."
                )
            } catch (e: java.io.EOFException) {
                Log.e("TallerViewModel", "Error de JSON truncado - respuesta incompleta", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Respuesta incompleta del servidor. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("TallerViewModel", "Error HTTP ${e.code()}: ${e.message()}", e)
                Log.e("TallerViewModel", "Error body: $errorBody")
                Log.e("TallerViewModel", "Request URL: ${e.response()?.raw()?.request?.url}")
                
                val errorMsg = when (e.code()) {
                    401 -> "Sesión expirada. Por favor inicia sesión nuevamente."
                    404 -> "Endpoint no encontrado. Verifica la configuración del servidor."
                    500 -> "Error interno del servidor. Revisa los logs del backend."
                    else -> "Error del servidor (${e.code()}): ${e.message()}"
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = errorMsg
                )
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("TallerViewModel", "Timeout de red", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Tiempo de espera agotado. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: java.net.UnknownHostException) {
                Log.e("TallerViewModel", "Error de conectividad", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Sin conexión a internet. Verifica tu red e intenta de nuevo."
                )
            } catch (e: Exception) {
                Log.e("TallerViewModel", "Excepción general", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error inesperado: ${e.localizedMessage ?: e.message ?: "Error desconocido"}"
                )
            }
        }
    }
    
    private fun refreshTalleres() {
        loadTalleres()
    }
    
    private fun loadTalleresByDate(fecha: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, hasError = false)
            
            try {
                // Cargar todos los talleres y filtrar por fecha
                val response = apiService.getTalleresActivos()
                
                if (response.isSuccessful() && response.data != null) {
                    val talleresList = response.data
                    val talleresFiltrados = talleresList.filter { taller ->
                        taller.fechaInicio.startsWith(fecha) // Filtrar por fecha
                    }
                    
                    _talleres.value = talleresFiltrados
                    _uiState.value = _uiState.value.copy(
                        talleres = talleresFiltrados,
                        isLoading = false,
                        hasError = false,
                        errorMessage = null
                    )
                    Log.d("TallerViewModel", "Talleres por fecha cargados: ${talleresFiltrados.size}")
                } else {
                    val errorMsg = response.getErrorMessage()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage = errorMsg
                    )
                    Log.e("TallerViewModel", "Error en respuesta por fecha: $errorMsg")
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("TallerViewModel", "Error de sintaxis JSON por fecha", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error de formato en la respuesta del servidor. Intenta de nuevo."
                )
            } catch (e: java.io.EOFException) {
                Log.e("TallerViewModel", "Error de JSON truncado por fecha", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Respuesta incompleta del servidor. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: retrofit2.HttpException) {
                Log.e("TallerViewModel", "Error HTTP ${e.code()} por fecha: ${e.message()}", e)
                val errorMsg = if (e.code() == 401) {
                    "Sesión expirada. Por favor inicia sesión nuevamente."
                } else {
                    "Error del servidor (${e.code()}). Intenta de nuevo más tarde."
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = errorMsg
                )
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("TallerViewModel", "Timeout al cargar talleres por fecha", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Tiempo de espera agotado. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: java.net.UnknownHostException) {
                Log.e("TallerViewModel", "Error de conectividad por fecha", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Sin conexión a internet. Verifica tu red e intenta de nuevo."
                )
            } catch (e: Exception) {
                Log.e("TallerViewModel", "Excepción general por fecha", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error inesperado: ${e.localizedMessage ?: e.message ?: "Error desconocido"}"
                )
            }
        }
    }
    
    private fun loadTalleresByType(tipo: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, hasError = false)
            
            try {
                val response = apiService.getTalleresByType(tipo)
                
                if (response.isSuccessful() && response.data != null) {
                    val talleresList = response.data
                    _talleres.value = talleresList
                    _uiState.value = _uiState.value.copy(
                        talleres = talleresList,
                        isLoading = false,
                        hasError = false,
                        errorMessage = null
                    )
                    Log.d("TallerViewModel", "Talleres por tipo cargados: ${talleresList.size}")
                } else {
                    val errorMsg = response.getErrorMessage()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage = errorMsg
                    )
                    Log.e("TallerViewModel", "Error en respuesta por tipo: $errorMsg")
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("TallerViewModel", "Error de sintaxis JSON por tipo", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error de formato en la respuesta del servidor. Intenta de nuevo."
                )
            } catch (e: java.io.EOFException) {
                Log.e("TallerViewModel", "Error de JSON truncado por tipo", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Respuesta incompleta del servidor. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: retrofit2.HttpException) {
                Log.e("TallerViewModel", "Error HTTP ${e.code()} por tipo: ${e.message()}", e)
                val errorMsg = if (e.code() == 401) {
                    "Sesión expirada. Por favor inicia sesión nuevamente."
                } else {
                    "Error del servidor (${e.code()}). Intenta de nuevo más tarde."
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = errorMsg
                )
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("TallerViewModel", "Timeout al cargar talleres por tipo", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Tiempo de espera agotado. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: java.net.UnknownHostException) {
                Log.e("TallerViewModel", "Error de conectividad por tipo", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Sin conexión a internet. Verifica tu red e intenta de nuevo."
                )
            } catch (e: Exception) {
                Log.e("TallerViewModel", "Excepción general por tipo", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error inesperado: ${e.localizedMessage ?: e.message ?: "Error desconocido"}"
                )
            }
        }
    }
    
    private fun loadTalleresActivos(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, hasError = false)
            
            try {
                val response = apiService.getTalleresByUser(userId)
                
                if (response.isSuccessful() && response.data != null) {
                    val talleresList = response.data
                    _talleres.value = talleresList
                    _uiState.value = _uiState.value.copy(
                        talleres = talleresList,
                        isLoading = false,
                        hasError = false,
                        errorMessage = null
                    )
                    Log.d("TallerViewModel", "Talleres del usuario cargados: ${talleresList.size}")
                } else {
                    val errorMsg = response.getErrorMessage()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        hasError = true,
                        errorMessage = errorMsg
                    )
                    Log.e("TallerViewModel", "Error en respuesta del usuario: $errorMsg")
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("TallerViewModel", "Error de sintaxis JSON del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error de formato en la respuesta del servidor. Intenta de nuevo."
                )
            } catch (e: java.io.EOFException) {
                Log.e("TallerViewModel", "Error de JSON truncado del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Respuesta incompleta del servidor. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: retrofit2.HttpException) {
                Log.e("TallerViewModel", "Error HTTP ${e.code()} del usuario: ${e.message()}", e)
                val errorMsg = if (e.code() == 401) {
                    "Sesión expirada. Por favor inicia sesión nuevamente."
                } else {
                    "Error del servidor (${e.code()}). Intenta de nuevo más tarde."
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = errorMsg
                )
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("TallerViewModel", "Timeout al cargar talleres del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Tiempo de espera agotado. Verifica tu conexión e intenta de nuevo."
                )
            } catch (e: java.net.UnknownHostException) {
                Log.e("TallerViewModel", "Error de conectividad del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Sin conexión a internet. Verifica tu red e intenta de nuevo."
                )
            } catch (e: Exception) {
                Log.e("TallerViewModel", "Excepción general del usuario", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Error inesperado: ${e.localizedMessage ?: e.message ?: "Error desconocido"}"
                )
            }
        }
    }
    
    fun getTalleresByFecha(fecha: String): StateFlow<List<TallerRecreativo>> {
        val filteredTalleres = MutableStateFlow<List<TallerRecreativo>>(emptyList())
        
        viewModelScope.launch {
            talleres.collect { allTalleres ->
                val filtered = allTalleres.filter { taller ->
                    taller.fechaInicio.startsWith(fecha)
                }
                filteredTalleres.value = filtered
            }
        }
        
        return filteredTalleres.asStateFlow()
    }
    
    /**
     * Método de prueba para verificar la conectividad con el servidor
     */
    fun testConnection() {
        viewModelScope.launch {
            try {
                Log.d("TallerViewModel", "Iniciando prueba de conectividad...")
                
                // Primero probamos el endpoint de prueba general
                val testResponse = apiService.testConnection()
                Log.d("TallerViewModel", "Test general exitoso: ${testResponse.data}")
                
                // Luego probamos específicamente el endpoint de talleres
                val tallerTestResponse = apiService.getTalleresActivos()
                if (tallerTestResponse.isSuccessful()) {
                    Log.d("TallerViewModel", "Test de talleres exitoso: ${tallerTestResponse.data?.size} talleres")
                } else {
                    Log.e("TallerViewModel", "Test de talleres falló: ${tallerTestResponse.getErrorMessage()}")
                }
                
            } catch (e: Exception) {
                Log.e("TallerViewModel", "Error en test de conectividad", e)
            }
        }
    }
} 
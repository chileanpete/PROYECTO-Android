package com.example.proyecto_droid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.RegistroConsumo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.proyecto_droid.data.model.Plato
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient
import java.io.IOException
import retrofit2.HttpException
import android.util.Log
import android.content.Context
import android.content.SharedPreferences
import java.io.File

sealed class RegistroConsumoUiState {
    object Loading : RegistroConsumoUiState()
    data class Success(val registros: List<RegistroConsumo>) : RegistroConsumoUiState()
    data class Error(val message: String) : RegistroConsumoUiState()
}

sealed class PlatosUiState {
    object Loading : PlatosUiState()
    data class Success(val platos: List<Plato>) : PlatosUiState()
    data class Error(val message: String) : PlatosUiState()
}

class RegistroConsumoViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<RegistroConsumoUiState>(RegistroConsumoUiState.Loading)
    val uiState: StateFlow<RegistroConsumoUiState> = _uiState

    val isSaving = MutableStateFlow(false)
    val saveError = MutableStateFlow<String?>(null)
    val deleteError = MutableStateFlow<String?>(null)
    val isExporting = MutableStateFlow(false)
    val exportError = MutableStateFlow<String?>(null)
    val pdfFile = MutableStateFlow<File?>(null)

    // Estados para manejo de platos con mejor UX
    private val _platosUiState = MutableStateFlow<PlatosUiState>(PlatosUiState.Loading)
    val platosUiState: StateFlow<PlatosUiState> = _platosUiState
    
    private val _platos = MutableStateFlow<List<Plato>>(emptyList())
    val platos: StateFlow<List<Plato>> = _platos

    private val service = UnifiedRetrofitClient.getApiService(application)

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private fun getUserId(): Int {
        return sharedPreferences.getInt("user_id", 1) // Default a 1 si no hay usuario guardado
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt("user_id", userId).apply()
        cargarRegistros(userId) // Recarga con el nuevo usuario
    }

    fun refreshRegistros() {
        cargarRegistros(getUserId())
    }
    
    fun refreshPlatos() {
        cargarPlatos()
    }

    init {
        cargarRegistros(getUserId())
        cargarPlatos()
    }

    fun cargarRegistros(idUsuario: Int = 1) {
        viewModelScope.launch {
            _uiState.value = RegistroConsumoUiState.Loading
            try {
                // Verificar si el usuario está autenticado
                val authManager = com.example.proyecto_droid.data.local.AuthManager(getApplication())
                val token = authManager.authToken.first()
                
                if (token.isNullOrEmpty()) {
                    Log.e("RegistroConsumoVM", "Usuario no autenticado - token vacío")
                    _uiState.value = RegistroConsumoUiState.Error("Usuario no autenticado. Por favor inicia sesión.")
                    return@launch
                }
                
                Log.d("RegistroConsumoVM", "Cargando registros para usuario: $idUsuario")
                val response = service.getConsumos(idUsuario, page = null)
                Log.d("RegistroConsumoVM", "Respuesta getConsumos: success=${response.success}")
                
                if (response.isSuccessful() && response.data != null) {
                    val registros = response.data.data
                    Log.d("RegistroConsumoVM", "Registros recibidos: ${registros.size} elementos")
                    _uiState.value = RegistroConsumoUiState.Success(registros)
                } else {
                    val errorMsg = response.getErrorMessage()
                    Log.e("RegistroConsumoVM", "Error en respuesta: $errorMsg")
                    _uiState.value = RegistroConsumoUiState.Error(errorMsg)
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("RegistroConsumoVM", "Error de parsing JSON - respuesta malformada", e)
                _uiState.value = RegistroConsumoUiState.Error("Error de formato en la respuesta del servidor. Intenta de nuevo.")
            } catch (e: java.io.EOFException) {
                Log.e("RegistroConsumoVM", "Error de JSON truncado - respuesta incompleta", e)
                _uiState.value = RegistroConsumoUiState.Error("Respuesta incompleta del servidor. Verifica tu conexión e intenta de nuevo.")
            } catch (e: retrofit2.HttpException) {
                Log.e("RegistroConsumoVM", "Error HTTP ${e.code()}: ${e.message()}", e)
                if (e.code() == 401) {
                    _uiState.value = RegistroConsumoUiState.Error("Sesión expirada. Por favor inicia sesión nuevamente.")
                } else {
                    _uiState.value = RegistroConsumoUiState.Error("Error del servidor (${e.code()}). Intenta de nuevo más tarde.")
                }
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("RegistroConsumoVM", "Timeout de red", e)
                _uiState.value = RegistroConsumoUiState.Error("Tiempo de espera agotado. Verifica tu conexión e intenta de nuevo.")
            } catch (e: java.net.UnknownHostException) {
                Log.e("RegistroConsumoVM", "Error de conectividad", e)
                _uiState.value = RegistroConsumoUiState.Error("Sin conexión a internet. Verifica tu red e intenta de nuevo.")
            } catch (e: Exception) {
                Log.e("RegistroConsumoVM", "Excepción general en cargarRegistros", e)
                _uiState.value = RegistroConsumoUiState.Error("Error inesperado: ${e.localizedMessage ?: e.message ?: "Error desconocido"}")
            }
        }
    }

    fun cargarPlatos() {
        viewModelScope.launch {
            _platosUiState.value = PlatosUiState.Loading
            try {
                Log.d("RegistroConsumoVM", "Iniciando carga de platos...")
                val response = service.getPlatos(page = null)
                Log.d("RegistroConsumoVM", "Respuesta recibida - success: ${response.success}")
                
                if (response.isSuccessful() && response.data != null) {
                    val lista = response.data.data
                    Log.d("RegistroConsumoVM", "Platos recibidos: ${lista.size} elementos")
                    _platos.value = lista
                    _platosUiState.value = PlatosUiState.Success(lista)
                } else {
                    val errorMsg = response.getErrorMessage()
                    Log.e("RegistroConsumoVM", "Error en respuesta getPlatos: $errorMsg")
                    _platos.value = emptyList()
                    _platosUiState.value = PlatosUiState.Error(errorMsg)
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e("RegistroConsumoVM", "Error de sintaxis JSON en cargarPlatos", e)
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("Error de formato en la respuesta del servidor. Intenta de nuevo.")
            } catch (e: java.io.EOFException) {
                Log.e("RegistroConsumoVM", "Error de JSON truncado en cargarPlatos - respuesta incompleta", e)
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("Respuesta incompleta del servidor. Verifica tu conexión e intenta de nuevo.")
                // Intentar reintentar después de un breve delay
                kotlinx.coroutines.delay(2000)
                Log.d("RegistroConsumoVM", "Reintentando carga de platos...")
                reintentarCargarPlatos()
            } catch (e: retrofit2.HttpException) {
                Log.e("RegistroConsumoVM", "Error HTTP ${e.code()} en cargarPlatos: ${e.message()}", e)
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("Error del servidor (${e.code()}). Intenta de nuevo más tarde.")
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("RegistroConsumoVM", "Timeout al cargar platos", e)
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("Tiempo de espera agotado. Verifica tu conexión e intenta de nuevo.")
            } catch (e: java.net.UnknownHostException) {
                Log.e("RegistroConsumoVM", "Error de conectividad al cargar platos", e)
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("Sin conexión a internet. Verifica tu red e intenta de nuevo.")
            } catch (e: Exception) {
                Log.e("RegistroConsumoVM", "Excepción general en cargarPlatos: ${e.javaClass.simpleName} - ${e.message}", e)
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("Error inesperado: ${e.localizedMessage ?: e.message ?: "Error desconocido"}")
            }
        }
    }
    
    private suspend fun reintentarCargarPlatos() {
        try {
            Log.d("RegistroConsumoVM", "Reintento de carga de platos con endpoint simplificado...")
            val response = service.getPlatosSimple() // Usar endpoint simplificado como fallback
            
            if (response.isSuccessful() && response.data != null) {
                val lista = response.data.data
                Log.d("RegistroConsumoVM", "Reintento exitoso con endpoint simplificado - Platos: ${lista.size}")
                _platos.value = lista
                _platosUiState.value = PlatosUiState.Success(lista)
            } else {
                Log.e("RegistroConsumoVM", "Reintento falló con endpoint simplificado: ${response.getErrorMessage()}")
                _platos.value = emptyList()
                _platosUiState.value = PlatosUiState.Error("No se pudieron cargar los platos. Intenta de nuevo más tarde.")
            }
        } catch (e: Exception) {
            Log.e("RegistroConsumoVM", "Error en reintento con endpoint simplificado: ${e.message}", e)
            _platos.value = emptyList()
            _platosUiState.value = PlatosUiState.Error("Error inesperado al cargar platos. Verifica tu conexión.")
        }
    }

    fun exportarPDF(context: Context) {
        isExporting.value = true
        exportError.value = null
        pdfFile.value = null
        viewModelScope.launch {
            try {
                Log.d("RegistroConsumoVM", "Iniciando exportación de PDF...")
                // TODO: Implementar exportación de PDF cuando esté disponible en el UnifiedApiService
                exportError.value = "Funcionalidad de exportación temporalmente deshabilitada"
                Log.e("RegistroConsumoVM", "Exportación de PDF no implementada en UnifiedApiService")
            } catch (e: Exception) {
                Log.e("RegistroConsumoVM", "Excepción durante exportación: ${e.localizedMessage}")
                exportError.value = "Error: ${e.localizedMessage}"
            } finally {
                isExporting.value = false
            }
        }
    }

    fun compartirPDF(context: Context) {
        val file = pdfFile.value
        if (file != null) {
            Log.d("RegistroConsumoVM", "Compartiendo PDF: ${file.absolutePath}")
            com.example.proyecto_droid.util.FileUtils.sharePdf(context, file)
        } else {
            Log.d("RegistroConsumoVM", "No hay archivo PDF para compartir")
        }
    }

    fun limpiarPDF() {
        pdfFile.value = null
        exportError.value = null
    }

    fun limpiarErroresExportacion() {
        exportError.value = null
    }

    fun crearRegistroConsumo(
        plato: Plato,
        porciones: Double,
        valoracion: Int?,
        comentario: String?,
        fecha: String,
        hora: String
    ) {
        isSaving.value = true
        saveError.value = null
        viewModelScope.launch {
            try {
                val nuevoConsumo = RegistroConsumo(
                    idUsuario = getUserId(),
                    idPlato = plato.id,
                    fechaConsumo = fecha,
                    horaConsumo = hora,
                    porciones = porciones,
                    cantidad = porciones,
                    caloriasTotales = (((plato.caloriasPorPorcion?.toDoubleOrNull() ?: 0.0) * porciones).toInt()),
                    valoracion = valoracion,
                    comentario = comentario,
                    plato = plato
                )
                Log.d("RegistroConsumoVM", "Enviando consumo: $nuevoConsumo")
                val response = service.createConsumo(nuevoConsumo)
                Log.d("RegistroConsumoVM", "Respuesta: $response")
                
                if (response.isSuccessful() && response.data != null) {
                    Log.d("RegistroConsumoVM", "Registro guardado exitosamente")
                    cargarRegistros(getUserId()) // Refresca la lista con el ID del usuario
                } else {
                    val errorMsg = response.getErrorMessage()
                    Log.e("RegistroConsumoVM", "Error final: $errorMsg")
                    saveError.value = errorMsg
                }
            } catch (e: IOException) {
                Log.e("RegistroConsumoVM", "Error de red al guardar", e)
                saveError.value = "Error de red: ${e.localizedMessage}"
            } catch (e: HttpException) {
                Log.e("RegistroConsumoVM", "Error HTTP al guardar", e)
                saveError.value = "Error HTTP: ${e.message}"
            } catch (e: Exception) {
                Log.e("RegistroConsumoVM", "Error general al guardar", e)
                saveError.value = "Error: ${e.localizedMessage}"
            } finally {
                isSaving.value = false
            }
        }
    }
} 
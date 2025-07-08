package com.example.proyecto_droid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.model.TipoEjercicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.proyecto_droid.data.network.RetrofitClient
import android.util.Log

sealed class RegistroActividadUiState {
    object Loading : RegistroActividadUiState()
    data class Success(val registros: List<RegistroActividad>) : RegistroActividadUiState()
    data class Error(val message: String) : RegistroActividadUiState()
}

class RegistroActividadViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RegistroActividadUiState>(RegistroActividadUiState.Loading)
    val uiState: StateFlow<RegistroActividadUiState> = _uiState

    val isSaving = MutableStateFlow(false)
    val saveError = MutableStateFlow<String?>(null)
    val deleteError = MutableStateFlow<String?>(null)
    val isExporting = MutableStateFlow(false)
    val exportError = MutableStateFlow<String?>(null)
    val pdfFile = MutableStateFlow<String?>(null)

    init {
        cargarRegistros()
    }

    fun cargarRegistros(idUsuario: Int = 1) {
        viewModelScope.launch {
            _uiState.value = RegistroActividadUiState.Loading
            try {
                val response = RetrofitClient.apiService.getRegistrosActividadFisica(idUsuario)
                if (response.isSuccessful) {
                    val registros = response.body()?.data ?: emptyList()
                    _uiState.value = RegistroActividadUiState.Success(registros)
                } else {
                    _uiState.value = RegistroActividadUiState.Error("Error al obtener registros: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = RegistroActividadUiState.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

    fun exportarPDF() {
        // Simulación de exportación
        isExporting.value = true
        viewModelScope.launch {
            // Simula un retardo
            kotlinx.coroutines.delay(1000)
            isExporting.value = false
            pdfFile.value = "ruta/del/archivo.pdf"
        }
    }

    fun confirmarEliminacion(id: Int) {
        // Simulación de eliminación
        deleteError.value = null
    }

    suspend fun crearRegistroActividad(
        idUsuario: Int,
        idTipoEjercicio: Int,
        idRutina: Int? = null,
        idRutinaEjercicio: Int? = null,
        fecha: String,
        horaInicio: String,
        horaFin: String,
        duracion: Int,
        caloriasQuemadas: Int,
        intensidad: Int?,
        comentario: String?,
        completada: Boolean = true
    ) {
        isSaving.value = true
        saveError.value = null
        try {
            val body = mutableMapOf<String, Any?>(
                "id_usuario" to idUsuario,
                "id_tipo_ejercicio" to idTipoEjercicio,
                "id_rutina" to idRutina,
                "id_rutina_ejercicio" to idRutinaEjercicio,
                "fecha_actividad" to fecha,
                "hora_inicio" to horaInicio,
                "hora_fin" to horaFin,
                "duracion_minutos" to duracion,
                "calorias_quemadas" to caloriasQuemadas,
                "intensidad" to (intensidad ?: 3),
                "comentario" to comentario,
                "completada" to completada
            )
            Log.d("RegistroAPI", "body: $body")
            val response = RetrofitClient.apiService.crearRegistroActividad(body)
            Log.d("RegistroAPI", "codigo: ${response.code()}")
            if (!response.isSuccessful) {
                Log.e("RegistroAPI", "error: ${response.errorBody()?.string()}")
            }
            if (response.isSuccessful) {
                cargarRegistros(idUsuario)
            } else {
                saveError.value = "Error al guardar: ${response.code()} ${response.message()} ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            saveError.value = "Error al guardar: ${e.localizedMessage}"
            Log.e("RegistroAPI", "error: ${e.localizedMessage}", e)
        } finally {
            isSaving.value = false
        }
    }
} 
package com.example.proyecto_droid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.RegistroActividad
import com.example.proyecto_droid.data.model.TipoEjercicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.proyecto_droid.data.repository.UnifiedActivityRepository
import android.util.Log
import android.content.Context
import com.example.proyecto_droid.util.FileUtils
import java.io.File
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient

sealed class RegistroActividadUiState {
    object Loading : RegistroActividadUiState()
    data class Success(val registros: List<RegistroActividad>) : RegistroActividadUiState()
    data class Error(val message: String) : RegistroActividadUiState()
}

class RegistroActividadViewModel(private val context: Context) : ViewModel() {
    private val activityRepository = UnifiedActivityRepository(context)
    
    private val _uiState = MutableStateFlow<RegistroActividadUiState>(RegistroActividadUiState.Loading)
    val uiState: StateFlow<RegistroActividadUiState> = _uiState

    val isSaving = MutableStateFlow(false)
    val saveError = MutableStateFlow<String?>(null)
    val deleteError = MutableStateFlow<String?>(null)
    val isExporting = MutableStateFlow(false)
    val exportError = MutableStateFlow<String?>(null)
    val pdfFile = MutableStateFlow<File?>(null)

    init {
        cargarRegistros()
    }

    fun cargarRegistros(idUsuario: Int = 1) {
        viewModelScope.launch {
            _uiState.value = RegistroActividadUiState.Loading
            
            activityRepository.getActividades(idUsuario).fold(
                onSuccess = { paginatedResponse ->
                    _uiState.value = RegistroActividadUiState.Success(paginatedResponse.data)
                },
                onFailure = { exception ->
                    _uiState.value = RegistroActividadUiState.Error(exception.message ?: "Error al obtener registros")
                }
            )
        }
    }

    fun exportarPDF(context: Context) {
        isExporting.value = true
        exportError.value = null
        pdfFile.value = null
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val idUsuario = sharedPreferences.getInt("user_id", 1)
        Log.d("RegistroActividadVM", "[exportarPDF] Iniciando exportación de PDF para usuario $idUsuario")
        
        viewModelScope.launch {
            try {
                Log.d("RegistroActividadVM", "[exportarPDF] Llamando a exportarPDFActividad()")
                val apiService = UnifiedRetrofitClient.getApiService(context)
                val response = apiService.exportarPDFActividad()
                Log.d("RegistroActividadVM", "[exportarPDF] Respuesta recibida: ${response.success}")
                
                if (response.isSuccessful() && response.data != null) {
                    val pdfData = response.data
                    Log.d("RegistroActividadVM", "[exportarPDF] pdfData: $pdfData")
                    
                    // ExportResponse contiene filename y downloadUrl, no content
                    Log.d("RegistroActividadVM", "[exportarPDF] PDF disponible para descarga: ${pdfData.filename}")
                    Log.d("RegistroActividadVM", "[exportarPDF] URL de descarga: ${pdfData.downloadUrl}")
                    
                    // Descargar PDF automáticamente al dispositivo
                    if (!pdfData.downloadUrl.isNullOrEmpty()) {
                        try {
                            Log.d("RegistroActividadVM", "[exportarPDF] Descargando PDF desde: ${pdfData.downloadUrl}")
                            
                            val file = FileUtils.downloadPdfFromUrl(
                                context, 
                                pdfData.downloadUrl, 
                                pdfData.filename
                            )
                            
                            if (file != null && file.exists()) {
                                pdfFile.value = file
                                Log.d("RegistroActividadVM", "[exportarPDF] PDF descargado exitosamente en: ${file.absolutePath}")
                                Toast.makeText(context, "PDF descargado en: Downloads/ProyectoDroid/${pdfData.filename}", Toast.LENGTH_LONG).show()
                                
                                // Intentar abrir el PDF automáticamente
                                try {
                                    FileUtils.openPdf(context, file)
                                } catch (e: Exception) {
                                    Log.w("RegistroActividadVM", "No se pudo abrir el PDF automáticamente: ${e.message}")
                                }
                            } else {
                                exportError.value = "Error al descargar el PDF del servidor"
                                Toast.makeText(context, "Error al descargar PDF", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Log.e("RegistroActividadVM", "[exportarPDF] Error durante descarga: ${e.message}", e)
                            exportError.value = "Error al descargar PDF: ${e.message}"
                            Toast.makeText(context, "Error al descargar PDF", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        exportError.value = "URL de descarga no disponible"
                        Toast.makeText(context, "Error: URL de descarga no disponible", Toast.LENGTH_LONG).show()
                    }
                    
                    exportError.value = null
                } else {
                    val errorMsg = response.getErrorMessage()
                    Log.e("RegistroActividadVM", "[exportarPDF] Error en respuesta: $errorMsg")
                    exportError.value = errorMsg
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("RegistroActividadVM", "[exportarPDF] Excepción: ${e.message}", e)
                exportError.value = "Error inesperado: ${e.message}"
                Toast.makeText(context, "Error inesperado al exportar PDF", Toast.LENGTH_LONG).show()
            } finally {
                isExporting.value = false
                Log.d("RegistroActividadVM", "[exportarPDF] Exportación finalizada")
            }
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
        
        val registroActividad = RegistroActividad(
            idUsuario = idUsuario,
            idTipoEjercicio = idTipoEjercicio,
            idRutinaEjercicio = idRutinaEjercicio,
            fechaActividad = fecha,
            horaInicio = horaInicio,
            horaFin = horaFin,
            duracionMinutos = duracion,
            caloriasQuemadas = caloriasQuemadas,
            intensidad = intensidad ?: 3,
            comentario = comentario,
            completada = completada
        )
        
        Log.d("RegistroAPI", "registroActividad: $registroActividad")
        
        activityRepository.createActividad(registroActividad).fold(
            onSuccess = { registroCreado ->
                Log.d("RegistroAPI", "Registro creado exitosamente: ${registroCreado.id}")
                cargarRegistros(idUsuario)
            },
            onFailure = { exception ->
                Log.e("RegistroAPI", "Error al crear registro: ${exception.message}")
                saveError.value = exception.message ?: "Error al guardar el registro"
            }
        )
        
        isSaving.value = false
    }
} 

// ViewModelFactory para manejar el constructor con parámetros
class RegistroActividadViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistroActividadViewModel::class.java)) {
            return RegistroActividadViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
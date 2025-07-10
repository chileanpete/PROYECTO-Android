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
                Log.d("RegistroActividadVM", "[exportarPDF] Llamando a RetrofitClient.apiService.exportarPDFActividad()")
                val response = RetrofitClient.apiService.exportarPDFActividad(idUsuario)
                Log.d("RegistroActividadVM", "[exportarPDF] Respuesta recibida: ${response.code()} ${response.message()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    val pdfData = response.body()?.data
                    Log.d("RegistroActividadVM", "[exportarPDF] pdfData: $pdfData")
                    if (pdfData != null) {
                        Log.d("RegistroActividadVM", "[exportarPDF] Guardando PDF en dispositivo...")
                        val file = com.example.proyecto_droid.util.FileUtils.savePdfToDevice(context, pdfData.content, pdfData.filename)
                        if (file != null) {
                            pdfFile.value = file
                            Log.d("RegistroActividadVM", "[exportarPDF] PDF guardado exitosamente: ${file.absolutePath}")
                            Toast.makeText(context, "PDF descargado en Descargas", Toast.LENGTH_LONG).show()
                        } else {
                            Log.e("RegistroActividadVM", "[exportarPDF] Error al guardar PDF en dispositivo")
                            exportError.value = "Error al guardar el PDF en el dispositivo"
                            Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Log.e("RegistroActividadVM", "[exportarPDF] No se recibieron datos del PDF")
                        exportError.value = "No se recibieron datos del PDF"
                        Toast.makeText(context, "No se recibieron datos del PDF", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "Error al generar PDF"
                    Log.e("RegistroActividadVM", "[exportarPDF] Error en respuesta: $errorMsg")
                    exportError.value = errorMsg
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                }
            )
            
            isExporting.value = false
            Log.d("RegistroActividadVM", "[exportarPDF] Exportación finalizada")
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
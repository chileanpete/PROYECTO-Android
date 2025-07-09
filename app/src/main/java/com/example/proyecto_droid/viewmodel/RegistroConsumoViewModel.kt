package com.example.proyecto_droid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.model.RegistroConsumo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.proyecto_droid.model.Plato
import com.example.proyecto_droid.data.network.services.CrearRegistroConsumoRequest
import com.example.proyecto_droid.data.network.services.RegistroConsumoService
import com.example.proyecto_droid.data.network.services.ApiResponse
import retrofit2.Response
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

class RegistroConsumoViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<RegistroConsumoUiState>(RegistroConsumoUiState.Loading)
    val uiState: StateFlow<RegistroConsumoUiState> = _uiState

    val isSaving = MutableStateFlow(false)
    val saveError = MutableStateFlow<String?>(null)
    val deleteError = MutableStateFlow<String?>(null)
    val isExporting = MutableStateFlow(false)
    val exportError = MutableStateFlow<String?>(null)
    val pdfFile = MutableStateFlow<File?>(null)

    private val _platos = MutableStateFlow<List<Plato>>(emptyList())
    val platos: StateFlow<List<Plato>> = _platos

    private val service: RegistroConsumoService = com.example.proyecto_droid.data.network.RetrofitClient.registroConsumoService

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

    init {
        cargarRegistros(getUserId())
        cargarPlatos()
    }

    fun cargarRegistros(idUsuario: Int = 1) {
        viewModelScope.launch {
            _uiState.value = RegistroConsumoUiState.Loading
            try {
                val response = service.getRegistrosConsumo(idUsuario)
                Log.d("RegistroConsumoVM", "Respuesta getRegistrosConsumo: ${response.body()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    val registros = response.body()?.data?.data ?: emptyList()
                    Log.d("RegistroConsumoVM", "Registros recibidos: $registros")
                    _uiState.value = RegistroConsumoUiState.Success(registros)
                } else {
                    Log.e("RegistroConsumoVM", "Error en getRegistrosConsumo: ${response.errorBody()?.string()}")
                    _uiState.value = RegistroConsumoUiState.Error(response.body()?.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                Log.e("RegistroConsumoVM", "Excepción en cargarRegistros", e)
                _uiState.value = RegistroConsumoUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun cargarPlatos() {
        viewModelScope.launch {
            try {
                val response = service.getPlatos()
                Log.d("RegistroConsumoVM", "Código de respuesta: ${response.code()}")
                Log.d("RegistroConsumoVM", "Respuesta exitosa: ${response.isSuccessful}")
                Log.d("RegistroConsumoVM", "Respuesta getPlatos: ${response.body()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    val lista = response.body()?.data?.data ?: emptyList()
                    Log.d("RegistroConsumoVM", "Platos recibidos: $lista")
                    _platos.value = lista
                } else {
                    Log.e("RegistroConsumoVM", "Error en respuesta getPlatos: ${response.errorBody()?.string()}")
                    _platos.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("RegistroConsumoVM", "Excepción en cargarPlatos", e)
                _platos.value = emptyList()
            }
        }
    }

    fun exportarPDF(context: Context) {
        isExporting.value = true
        exportError.value = null
        pdfFile.value = null
        viewModelScope.launch {
            try {
                Log.d("RegistroConsumoVM", "Iniciando exportación de PDF...")
                val response = service.exportarPDF()
                Log.d("RegistroConsumoVM", "Respuesta recibida: ${response.code()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    val pdfData = response.body()?.data
                    if (pdfData != null) {
                        Log.d("RegistroConsumoVM", "Datos PDF recibidos, guardando archivo...")
                        val file = com.example.proyecto_droid.util.FileUtils.savePdfToDevice(context, pdfData.content, pdfData.filename)
                        if (file != null) {
                            pdfFile.value = file
                            Log.d("RegistroConsumoVM", "PDF guardado exitosamente: ${file.absolutePath}")
                        } else {
                            Log.e("RegistroConsumoVM", "Error al guardar PDF en dispositivo")
                            exportError.value = "Error al guardar el PDF en el dispositivo"
                        }
                    } else {
                        Log.e("RegistroConsumoVM", "No se recibieron datos del PDF")
                        exportError.value = "No se recibieron datos del PDF"
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "Error al generar PDF"
                    Log.e("RegistroConsumoVM", "Error en respuesta: $errorMsg")
                    exportError.value = errorMsg
                }
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
                val request = CrearRegistroConsumoRequest(
                    id_plato = plato.idPlato ?: 0,
                    fecha_consumo = fecha,
                    hora_consumo = hora,
                    porciones = porciones,
                    valoracion = valoracion,
                    comentario = comentario,
                    calorias_totales = (((plato.caloriasPorPorcion?.toDoubleOrNull() ?: 0.0) * porciones).toInt())
                )
                Log.d("RegistroConsumoVM", "Enviando request: $request")
                val response: Response<ApiResponse<RegistroConsumo>> = service.crearRegistroConsumo(request)
                Log.d("RegistroConsumoVM", "Código de respuesta: ${response.code()}")
                Log.d("RegistroConsumoVM", "Respuesta exitosa: ${response.isSuccessful}")
                Log.d("RegistroConsumoVM", "Body de respuesta: ${response.body()}")
                Log.d("RegistroConsumoVM", "Error body: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d("RegistroConsumoVM", "Registro guardado exitosamente")
                    cargarRegistros(getUserId()) // Refresca la lista con el ID del usuario
                } else {
                    var errorMsg: String? = null
                    response.errorBody()?.let { errorBody ->
                        errorMsg = errorBody.string()
                        Log.e("RegistroConsumoVM", "Error body: $errorMsg")
                    }
                    if (errorMsg.isNullOrBlank()) {
                        val apiMsg = response.body()?.message
                        val apiErrors = response.body()?.errors
                        errorMsg = buildString {
                            if (!apiMsg.isNullOrBlank()) append(apiMsg)
                            if (apiErrors != null && apiErrors.isNotEmpty()) {
                                append("\n")
                                apiErrors.forEach { (field, msgs) ->
                                    append("$field: ")
                                    append(msgs.joinToString(", "))
                                    append("\n")
                                }
                            }
                        }.ifBlank { "Error desconocido al guardar" }
                    }
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
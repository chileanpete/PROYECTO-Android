package com.example.proyecto_droid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.remote.ApiClient
import com.example.proyecto_droid.model.RegistroConsumo
import com.example.proyecto_droid.model.Plato
import com.example.proyecto_droid.network.services.CrearRegistroConsumoRequest
import com.example.proyecto_droid.network.services.RegistroConsumoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import org.json.JSONObject
import com.example.proyecto_droid.util.FileUtils
import java.io.File
import android.content.Context

sealed class RegistroConsumoUiState {
    object Loading : RegistroConsumoUiState()
    data class Success(val registros: List<RegistroConsumo>) : RegistroConsumoUiState()
    data class Error(val message: String) : RegistroConsumoUiState()
    data class DeleteConfirmation(val id: Int) : RegistroConsumoUiState()
    data class DeleteSuccess(val message: String) : RegistroConsumoUiState()
}

class RegistroConsumoViewModel(app: Application) : AndroidViewModel(app) {
    private val context = app.applicationContext
    private val service: RegistroConsumoService =
        ApiClient.create(context).create(RegistroConsumoService::class.java)

    private val _uiState = MutableStateFlow<RegistroConsumoUiState>(RegistroConsumoUiState.Loading)
    val uiState: StateFlow<RegistroConsumoUiState> = _uiState.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    private val _exportError = MutableStateFlow<String?>(null)
    val exportError: StateFlow<String?> = _exportError.asStateFlow()

    private val _pdfFile = MutableStateFlow<File?>(null)
    val pdfFile: StateFlow<File?> = _pdfFile.asStateFlow()

    init {
        cargarRegistros()
    }

    fun cargarRegistros() {
        _uiState.value = RegistroConsumoUiState.Loading
        viewModelScope.launch {
            try {
                val response = service.getRegistrosConsumo()
                if (response.isSuccessful && response.body()?.success == true) {
                    val registros = response.body()?.data ?: emptyList()
                    _uiState.value = RegistroConsumoUiState.Success(registros)
                } else {
                    _uiState.value = RegistroConsumoUiState.Error(response.body()?.message ?: "Error desconocido")
                }
            } catch (e: IOException) {
                _uiState.value = RegistroConsumoUiState.Error("Error de red: ${e.localizedMessage}")
            } catch (e: HttpException) {
                _uiState.value = RegistroConsumoUiState.Error("Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = RegistroConsumoUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun crearRegistroConsumo(
        plato: Plato,
        porciones: Double,
        valoracion: Int?,
        comentario: String?,
        fecha: String,
        hora: String
    ) {
        _isSaving.value = true
        _saveError.value = null
        viewModelScope.launch {
            try {
                val request = CrearRegistroConsumoRequest(
                    id_plato = plato.idPlato,
                    fecha_consumo = fecha,
                    hora_consumo = hora,
                    porciones = porciones,
                    valoracion = valoracion,
                    comentario = comentario,
                    calorias_totales = (plato.caloriasPorPorcion * porciones).toInt()
                )
                println("Enviando registro: $request")
                val response = service.crearRegistroConsumo(request)
                println("Código de respuesta: ${response.code()}, body: ${response.body()}, errorBody: ${response.errorBody()?.string()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    cargarRegistros() // Refresca la lista
                } else {
                    var errorMsg: String? = null
                    response.errorBody()?.let { errorBody ->
                        val errorStr = errorBody.string()
                        try {
                            val json = org.json.JSONObject(errorStr)
                            errorMsg = buildString {
                                val msg = json.optString("message")
                                if (msg.isNotBlank()) append(msg)
                                val errors = json.optJSONObject("errors")
                                if (errors != null) {
                                    append("\n")
                                    errors.keys().forEach { key ->
                                        val arr = errors.getJSONArray(key)
                                        append("$key: ")
                                        for (i in 0 until arr.length()) {
                                            append(arr.getString(i))
                                            if (i < arr.length() - 1) append(", ")
                                        }
                                        append("\n")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            errorMsg = "Error desconocido al guardar"
                        }
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
                    println("Mensaje de error mostrado: $errorMsg")
                    _saveError.value = errorMsg
                }
            } catch (e: IOException) {
                println("Error de red al guardar: ${e.localizedMessage}")
                _saveError.value = "Error de red: ${e.localizedMessage}"
            } catch (e: HttpException) {
                println("Error HTTP al guardar: ${e.message}")
                _saveError.value = "Error HTTP: ${e.message}"
            } catch (e: Exception) {
                println("Error general al guardar: ${e.localizedMessage}")
                _saveError.value = "Error: ${e.localizedMessage}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun confirmarEliminacion(id: Int) {
        _uiState.value = RegistroConsumoUiState.DeleteConfirmation(id)
    }

    fun eliminarRegistroConsumo(id: Int) {
        viewModelScope.launch {
            try {
                println("Intentando eliminar registro con ID: $id")
                val response = service.eliminarRegistroConsumo(id)
                println("Código de respuesta: ${response.code()}")
                println("Body: ${response.body()}")
                println("Error body: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.value = RegistroConsumoUiState.DeleteSuccess("Registro eliminado exitosamente")
                    cargarRegistros()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.body()?.message ?: "Error al eliminar el registro"
                    println("Error al eliminar: $errorMsg")
                    _deleteError.value = errorMsg
                }
            } catch (e: Exception) {
                println("Excepción al eliminar: ${e.localizedMessage}")
                _deleteError.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun limpiarMensajes() {
        _deleteError.value = null
        if (_uiState.value is RegistroConsumoUiState.DeleteSuccess) {
            cargarRegistros()
        }
    }

    fun exportarPDF() {
        _isExporting.value = true
        _exportError.value = null
        _pdfFile.value = null
        
        viewModelScope.launch {
            try {
                println("Iniciando exportación de PDF...")
                val response = service.exportarPDF()
                println("Respuesta recibida: ${response.code()}")
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val pdfData = response.body()?.data
                    if (pdfData != null) {
                        println("Datos PDF recibidos, guardando archivo...")
                        // Guardar PDF en el dispositivo
                        val file = FileUtils.savePdfToDevice(context, pdfData.content, pdfData.filename)
                        if (file != null) {
                            _pdfFile.value = file
                            println("PDF guardado exitosamente: ${file.absolutePath}")
                        } else {
                            println("Error al guardar PDF en dispositivo")
                            _exportError.value = "Error al guardar el PDF en el dispositivo"
                        }
                    } else {
                        println("No se recibieron datos del PDF")
                        _exportError.value = "No se recibieron datos del PDF"
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "Error al generar PDF"
                    println("Error en respuesta: $errorMsg")
                    _exportError.value = errorMsg
                }
            } catch (e: Exception) {
                println("Excepción durante exportación: ${e.localizedMessage}")
                _exportError.value = "Error: ${e.localizedMessage}"
            } finally {
                _isExporting.value = false
            }
        }
    }

    fun compartirPDF(context: Context) {
        val file = _pdfFile.value
        if (file != null) {
            println("Compartiendo PDF: ${file.absolutePath}")
            FileUtils.sharePdf(context, file)
        } else {
            println("No hay archivo PDF para compartir")
        }
    }

    fun limpiarPDF() {
        _pdfFile.value = null
        _exportError.value = null
    }

    fun limpiarErroresExportacion() {
        _exportError.value = null
    }
}
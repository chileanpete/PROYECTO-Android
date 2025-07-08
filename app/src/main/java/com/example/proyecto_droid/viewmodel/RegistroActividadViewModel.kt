package com.example.proyecto_droid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.remote.ApiClient
import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.network.services.CrearRegistroActividadRequest
import com.example.proyecto_droid.network.services.RegistroActividadService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import org.json.JSONObject

sealed class RegistroActividadUiState {
    object Loading : RegistroActividadUiState()
    data class Success(val registros: List<RegistroActividad>) : RegistroActividadUiState()
    data class Error(val message: String) : RegistroActividadUiState()
    data class DeleteConfirmation(val id: Int) : RegistroActividadUiState()
    data class DeleteSuccess(val message: String) : RegistroActividadUiState()
}

class RegistroActividadViewModel(app: Application) : AndroidViewModel(app) {
    private val context = app.applicationContext
    private val service: RegistroActividadService =
        ApiClient.create(context).create(RegistroActividadService::class.java)

    private val _uiState = MutableStateFlow<RegistroActividadUiState>(RegistroActividadUiState.Loading)
    val uiState: StateFlow<RegistroActividadUiState> = _uiState.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError.asStateFlow()

    init {
        cargarRegistros()
    }

    fun cargarRegistros() {
        _uiState.value = RegistroActividadUiState.Loading
        viewModelScope.launch {
            try {
                val response = service.getRegistrosActividad()
                if (response.isSuccessful && response.body()?.success == true) {
                    val registros = response.body()?.data ?: emptyList()
                    _uiState.value = RegistroActividadUiState.Success(registros)
                } else {
                    _uiState.value = RegistroActividadUiState.Error(response.body()?.message ?: "Error desconocido")
                }
            } catch (e: IOException) {
                _uiState.value = RegistroActividadUiState.Error("Error de red: ${e.localizedMessage}")
            } catch (e: HttpException) {
                _uiState.value = RegistroActividadUiState.Error("Error HTTP: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = RegistroActividadUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun crearRegistroActividad(
        idTipoEjercicio: Int?,
        idRutina: Int?,
        fecha: String,
        horaInicio: String,
        horaFin: String,
        duracion: Int,
        caloriasQuemadas: Int,
        intensidad: Int,
        comentario: String?,
        completada: Boolean = true
    ) {
        _isSaving.value = true
        _saveError.value = null
        viewModelScope.launch {
            try {
                val request = CrearRegistroActividadRequest(
                    id_tipo_ejercicio = idTipoEjercicio,
                    id_rutina = idRutina,
                    fecha_actividad = fecha,
                    hora_inicio = horaInicio,
                    hora_fin = horaFin,
                    duracion_minutos = duracion,
                    calorias_quemadas = caloriasQuemadas,
                    intensidad = intensidad,
                    comentario = comentario,
                    completada = completada
                )
                println("Enviando registro actividad: $request")
                val response = service.crearRegistroActividad(request)
                println("Código de respuesta: ${response.code()}, body: ${response.body()}, errorBody: ${response.errorBody()?.string()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    cargarRegistros() // Refresca la lista
                } else {
                    var errorMsg: String? = null
                    response.errorBody()?.let { errorBody ->
                        val errorStr = errorBody.string()
                        try {
                            val json = JSONObject(errorStr)
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
        _uiState.value = RegistroActividadUiState.DeleteConfirmation(id)
    }

    fun eliminarRegistroActividad(id: Int) {
        viewModelScope.launch {
            try {
                println("Intentando eliminar registro de actividad con ID: $id")
                val response = service.eliminarRegistroActividad(id)
                println("Código de respuesta: ${response.code()}")
                println("Body: ${response.body()}")
                println("Error body: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.value = RegistroActividadUiState.DeleteSuccess("Registro eliminado exitosamente")
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
        if (_uiState.value is RegistroActividadUiState.DeleteSuccess) {
            cargarRegistros()
        }
    }
} 
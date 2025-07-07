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

sealed class RegistroConsumoUiState {
    object Loading : RegistroConsumoUiState()
    data class Success(val registros: List<RegistroConsumo>) : RegistroConsumoUiState()
    data class Error(val message: String) : RegistroConsumoUiState()
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
} 
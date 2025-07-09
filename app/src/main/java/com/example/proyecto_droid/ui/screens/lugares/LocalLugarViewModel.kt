package com.example.proyecto_droid.ui.screens.lugares

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.protobuf.Timestamp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.LugarRepository
import com.example.proyecto_droid.data.model.Lugar
import com.example.proyecto_droid.data.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class LocalLugarViewModel(
    private val lugarRepository: LugarRepository
) : ViewModel() {
    // Estado para la lista de lugares
    private val _lugares = MutableStateFlow<List<Lugar>>(emptyList())
    val lugares: StateFlow<List<Lugar>> get() = _lugares

    // Estado para el formulario (usando StateFlow)
    private val _uiState = MutableStateFlow(LugarUiState())
    val uiState: StateFlow<LugarUiState> = _uiState.asStateFlow()

    // Actualizar estado del formulario
    fun updateUiState(newLugarData: LugarData) {
        _uiState.update { currentState ->
            currentState.copy(
                lugarData = newLugarData,
                isEntryValid = validateInput(newLugarData)
            )
        }
    }

    private fun validateInput(lugarData: LugarData): Boolean {
        return lugarData.nombre.isNotBlank()
    }

    // Guardar el lugar
    fun saveLugar() {
        viewModelScope.launch {
            lugarRepository.insertLugar(uiState.value.lugarData.toLugar())
        }
    }
    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch {
            lugarRepository.deleteLugar(lugar)
        }
    }

    init {
        viewModelScope.launch {
            lugarRepository.getAllLugarStream().collect { lugaresList ->
                _lugares.value = lugaresList
            }
        }
    }
}


data class LugarUiState(
    val lugarData: LugarData = LugarData(),
    val isEntryValid: Boolean = false
)

data class LugarData(
    val id: Int = 0,  // AutoGenerate requiere valor por defecto
    val nombre: String ="",
    val tipo: String ="",
    val ubicacion: String? = null,  // Opcional
    val coordenadas_lat: Double? = null,
    val coordenadas_long: Double? = null,
    val hora_apertura: Timestamp? = null,
    val hora_cierre: Timestamp? = null,
    val telefono: String? = null,
    val calificacion_promedio: Double? = null,
    val precio_medio: Double? = null,
    val activo: Boolean = true,  // Valor por defecto
    val imagen_url: String? = null
)

fun LugarData.toLugar(): Lugar = Lugar(
    id = id,
    nombre = nombre,
    ubicacion= ubicacion,
    coordenadas_lat = coordenadas_lat,
    coordenadas_long = coordenadas_long,
    hora_apertura = hora_apertura,
    hora_cierre = hora_cierre,
    telefono = telefono,
    calificacion_promedio = calificacion_promedio,
    precio_medio = precio_medio,
    tipo = tipo,
    activo = activo,
    imagen_url = imagen_url
)

fun Lugar.toUiState(isEntryValid: Boolean = false): LugarUiState = LugarUiState(
    lugarData = LugarData(
        id = id,
        nombre = nombre,
        ubicacion= ubicacion,
        coordenadas_lat = coordenadas_lat,
        coordenadas_long = coordenadas_long,
        hora_apertura = hora_apertura,
        hora_cierre = hora_cierre,
        telefono = telefono,
        calificacion_promedio = calificacion_promedio,
        precio_medio = precio_medio,
        tipo = tipo,
        activo = activo,
        imagen_url = imagen_url),
    isEntryValid = isEntryValid
)
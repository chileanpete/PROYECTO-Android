package com.example.proyecto_droid.ui.screens.lugares

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.LugarRepository
import com.example.proyecto_droid.data.model.Lugar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class LocalLugarViewModel(private val lugarRepository: LugarRepository) : ViewModel() {

    private val _lugares = MutableStateFlow<List<Lugar>>(emptyList())
    val lugares: StateFlow<List<Lugar>> get() = _lugares


    var lugarUiState by mutableStateOf(LugarUiState())
        private set

    init {
        viewModelScope.launch {
            // Inserta las categorías por defecto si la tabla está vacía
            val lugares = lugarRepository.getAllLugarStream().first()
            if (lugares.isEmpty()) {
                lugarRepository.insertLugar(Lugar(nombre = "Peliculas", descripcion = "Categoría de películas"))
                lugarRepository.insertLugar(Lugar(nombre = "Series", descripcion = "Categoría de series"))
                lugarRepository.insertLugar(Lugar(nombre = "Anime", descripcion = "Categoría de anime"))
            }

            lugarRepository.getAllLugarStream().collect { lista ->
                _lugares.value = lista
            }
        }
    }



    fun updateUiState(nuevaLugar: LugarData) {
        lugarUiState = LugarUiState(
            lugarData = nuevaLugar,
            isEntryValid = validateInput(nuevaLugar)
        )
    }

    fun saveLugar() {
        if (validateInput()) {
            viewModelScope.launch {
                val lugar = lugarUiState.lugarData.toLugar()
                lugarRepository.insertLugar(lugar)
            }
        }
    }
    fun deleteLugar(lugar: Lugar) {
        viewModelScope.launch {
            lugarRepository.deleteLugar(lugar)
        }
    }


    private fun validateInput(lugar: LugarData = lugarUiState.lugarData): Boolean {
        return lugar.nombre.isNotBlank() && lugar.descripcion.isNotBlank()
    }
}



data class LugarUiState(
    val lugarData: LugarData = LugarData(),
    val isEntryValid: Boolean = false
)

data class LugarData(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)

fun LugarData.toLugar(): Lugar = Lugar(
    id = id,
    nombre = nombre,
    descripcion = descripcion
)

fun Lugar.toUiState(isEntryValid: Boolean = false): LugarUiState = LugarUiState(
    lugarData = LugarData(id, nombre, descripcion),
    isEntryValid = isEntryValid
)
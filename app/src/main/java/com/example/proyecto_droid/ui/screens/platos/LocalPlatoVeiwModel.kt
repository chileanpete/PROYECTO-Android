package com.example.proyecto_droid.ui.screens.platos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.PlatoRepository
import com.example.proyecto_droid.data.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class LocalPlatoVeiwModel(private val platoRepository: PlatoRepository) : ViewModel() {

    private val _platos = MutableStateFlow<List<Plato>>(emptyList())
    val platos: StateFlow<List<Plato>> get() = _platos


    var platoUiState by mutableStateOf(PlatoUiState())
        private set

    init {
        viewModelScope.launch {
            // Inserta las categorías por defecto si la tabla está vacía
            val platos =platoRepository.getAllPlatoStream().first()
            if (platos.isEmpty()) {
                platoRepository.insertPlato(Plato(nombrePlato = "Peliculas", descripcionPlato = "Categoría de películas"))
                platoRepository.insertPlato(Plato(nombrePlato = "Series", descripcionPlato = "Categoría de series"))
                platoRepository.insertPlato(Plato(nombrePlato = "Anime", descripcionPlato = "Categoría de anime"))
            }

            platoRepository.getAllPlatoStream().collect { lista ->
                _platos.value = lista
            }
        }
    }



    fun updateUiState(nuevaPlato: PlatoData) {
        platoUiState = PlatoUiState(
            platoData = nuevaPlato,
            isEntryValid = validateInput(nuevaPlato)
        )
    }

    fun savePlato() {
        if (validateInput()) {
            viewModelScope.launch {
                val plato = platoUiState.platoData.toPlato()
                platoRepository.insertPlato(plato)
            }
        }
    }
    fun deletePlato(plato: Plato) {
        viewModelScope.launch {
            platoRepository.deletePlato(plato)
        }
    }


    private fun validateInput(plato: PlatoData = platoUiState.platoData): Boolean {
        return plato.nombre.isNotBlank() && plato.descripcion.isNotBlank()
    }
}



data class PlatoUiState(
    val platoData: PlatoData = PlatoData(),
    val isEntryValid: Boolean = false
)

data class PlatoData(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)

fun PlatoData.toPlato(): Plato = Plato(
    id = id,
    nombrePlato = nombre,
    descripcionPlato = descripcion
)

fun Plato.toUiState(isEntryValid: Boolean = false): PlatoUiState = PlatoUiState(
    platoData = PlatoData(id, nombrePlato, descripcionPlato),
    isEntryValid = isEntryValid
)
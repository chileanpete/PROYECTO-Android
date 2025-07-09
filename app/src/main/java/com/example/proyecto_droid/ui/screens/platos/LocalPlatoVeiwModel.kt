package com.example.proyecto_droid.ui.screens.platos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.Categoria
import com.example.proyecto_droid.data.model.CategoriaRepository
import com.example.proyecto_droid.data.model.Lugar
import com.example.proyecto_droid.data.model.LugarRepository
import com.example.proyecto_droid.data.model.PlatoRepository
import com.example.proyecto_droid.data.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

class LocalPlatoVeiwModel(
    private val platoRepository: PlatoRepository,
    private val lugarRepository: LugarRepository,
    private val categoriaRepository: CategoriaRepository
    ) : ViewModel() {
    private val _platos = MutableStateFlow<List<Plato>>(emptyList())
    val platos: StateFlow<List<Plato>> get() = _platos
    var platoUiState by mutableStateOf(PlatoUiState())
        private set

    // Listas de lugares y categorías para el formulario
    val lugares: StateFlow<List<Lugar>> = lugarRepository.getAllLugarStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categorias: StateFlow<List<Categoria>> = categoriaRepository.getAllCategoriaStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    init {
        viewModelScope.launch {
            platoRepository.getAllPlatoStream().collect { lista ->
                _platos.value = lista
            }
        }
    }
//    init {
//        viewModelScope.launch {
//            // Inserta las categorías por defecto si la tabla está vacía
//            val platos =platoRepository.getAllPlatoStream().first()
//            if (platos.isEmpty()) {
//                platoRepository.insertPlato(Plato(nombrePlato = "Peliculas", descripcionPlato = "Categoría de películas"))
//                platoRepository.insertPlato(Plato(nombrePlato = "Series", descripcionPlato = "Categoría de series"))
//                platoRepository.insertPlato(Plato(nombrePlato = "Anime", descripcionPlato = "Categoría de anime"))
//            }
//
//            platoRepository.getAllPlatoStream().collect { lista ->
//                _platos.value = lista
//            }
//        }
//    }



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
        return plato.nombre.isNotBlank() && plato.descripcion?.isNotBlank() == true
    }
}



data class PlatoUiState(
    val platoData: PlatoData = PlatoData(),
    val isEntryValid: Boolean = false
)

data class PlatoData(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String? = null,
    val precio: Double = 0.0,
    val idLugar: Int? = null,  // Opcional
    val idCategoria: Int? = null,  // Requerido
    val esVegetariano: Boolean = false,
    val esVegano: Boolean = false,
    val imagenUrl: String? = null
)

fun PlatoData.toPlato(): Plato = Plato(
    id = id,
    nombrePlato = nombre,
    descripcionPlato = descripcion,
    precioPlato = precio,
    idLugar = idLugar,
    idCategoria = idCategoria ?: throw IllegalArgumentException("Categoría es requerida"),
    esVegetariano = esVegetariano,
    esVegano = esVegano,
    imagenUrl = imagenUrl
    // ... otros campos con valores por defecto
)

fun Plato.toUiState(isEntryValid: Boolean = false): PlatoUiState = PlatoUiState(
    platoData = PlatoData(
        id = id,
        nombre = nombrePlato,
        descripcion = descripcionPlato,
        precio = precioPlato,
        idLugar = idLugar,
        idCategoria = idCategoria,
        esVegetariano = esVegetariano,
        esVegano = esVegano,
        imagenUrl = imagenUrl
    ),
    isEntryValid = isEntryValid
)
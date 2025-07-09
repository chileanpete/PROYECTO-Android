package com.example.proyecto_droid.ui.screens.categorias

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.CategoriaRepository
import com.example.proyecto_droid.data.model.Categoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class CategoriaViewModel(private val categoriaRepository: CategoriaRepository) : ViewModel() {

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> get() = _categorias


    var categoriaUiState by mutableStateOf(CategoriaUiState())
        private set

    init {
        viewModelScope.launch {
            // Inserta las categorías por defecto si la tabla está vacía
            val categorias = categoriaRepository.getAllCategoriaStream().first()
            if (categorias.isEmpty()) {
                categoriaRepository.insertCategoria(Categoria(nombre = "Peliculas", descripcion = "Categoría de películas"))
                categoriaRepository.insertCategoria(Categoria(nombre = "Series", descripcion = "Categoría de series"))
                categoriaRepository.insertCategoria(Categoria(nombre = "Anime", descripcion = "Categoría de anime"))
            }

            categoriaRepository.getAllCategoriaStream().collect { lista ->
                _categorias.value = lista
            }
        }
    }



    fun updateUiState(nuevaCategoria: CategoriaData) {
        categoriaUiState = CategoriaUiState(
            categoriaData = nuevaCategoria,
            isEntryValid = validateInput(nuevaCategoria)
        )
    }

    fun saveCategoria() {
        if (validateInput()) {
            viewModelScope.launch {
                val categoria = categoriaUiState.categoriaData.toCategoria()
                categoriaRepository.insertCategoria(categoria)
            }
        }
    }
    fun deleteCategoria(categoria: Categoria) {
        viewModelScope.launch {
            categoriaRepository.deleteCategoria(categoria)
        }
    }


    private fun validateInput(categoria: CategoriaData = categoriaUiState.categoriaData): Boolean {
        return categoria.nombre.isNotBlank() && categoria.descripcion.isNotBlank()
    }
}



data class CategoriaUiState(
    val categoriaData: CategoriaData = CategoriaData(),
    val isEntryValid: Boolean = false
)

data class CategoriaData(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)

fun CategoriaData.toCategoria(): Categoria = Categoria(
    id = id,
    nombre = nombre,
    descripcion = descripcion
)

fun Categoria.toUiState(isEntryValid: Boolean = false): CategoriaUiState = CategoriaUiState(
    categoriaData = CategoriaData(id, nombre, descripcion),
    isEntryValid = isEntryValid
)
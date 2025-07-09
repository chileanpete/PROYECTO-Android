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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class CategoriaViewModel(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {
    // Estado único para el formulario
    private val _formState = MutableStateFlow(CategoriaUiState())
    val formState: StateFlow<CategoriaUiState> = _formState.asStateFlow()

    // Estado para la lista de categorías
    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    init {
        viewModelScope.launch {
            // Cargar categorías existentes
            categoriaRepository.getAllCategoriaStream().collect { lista ->
                _categorias.value = lista
            }

            // Insertar categorías por defecto si la tabla está vacía
            if (_categorias.value.isEmpty()) {
                val defaultCategories = listOf(
                    Categoria(nombre = "Peliculas", descripcion = "Categoría de películas"),
                    Categoria(nombre = "Series", descripcion = "Categoría de series"),
                    Categoria(nombre = "Anime", descripcion = "Categoría de anime")
                )
                defaultCategories.forEach { categoriaRepository.insertCategoria(it) }
            }
        }
    }

    fun updateUiState(newCategoria: CategoriaData) {
        _formState.update { currentState ->
            currentState.copy(
                categoriaData = newCategoria,
                isEntryValid = validateInput(newCategoria)
            )
        }
    }

    fun saveCategoria() {
        if (_formState.value.isEntryValid) {
            viewModelScope.launch {
                categoriaRepository.insertCategoria(_formState.value.categoriaData.toCategoria())
                // Limpiar el formulario después de guardar
                _formState.value = CategoriaUiState()
            }
        }
    }

    fun deleteCategoria(categoria: Categoria) {
        viewModelScope.launch {
            categoriaRepository.deleteCategoria(categoria)
        }
    }

    private fun validateInput(categoria: CategoriaData = _formState.value.categoriaData): Boolean {
        return categoria.nombre.isNotBlank() && categoria.descripcion?.isNotBlank() == true
    }
}


data class CategoriaUiState(
    val categoriaData: CategoriaData = CategoriaData(),
    val isEntryValid: Boolean = false
)

data class CategoriaData(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String? = null,
    val imUrl: String? = null
)

fun CategoriaData.toCategoria(): Categoria = Categoria(
    id_categoria = id,
    nombre = nombre,
    descripcion = descripcion,
    imagen_url = imUrl
)

fun Categoria.toUiState(isEntryValid: Boolean = false): CategoriaUiState = CategoriaUiState(
    categoriaData = CategoriaData(id_categoria, nombre, descripcion),
    isEntryValid = isEntryValid
)
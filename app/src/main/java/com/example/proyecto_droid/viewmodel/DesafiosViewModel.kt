package com.example.proyecto_droid.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.model.*
import com.example.proyecto_droid.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DesafiosViewModel : ViewModel() {

    private val _desafios = MutableStateFlow<List<Desafio>>(emptyList())
    val desafios: StateFlow<List<Desafio>> = _desafios

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarDesafios() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getDesafios()
                _desafios.value = response.map {
                    Desafio(
                        id = it.id,
                        titulo = it.titulo,
                        descripcion = it.descripcion,
                        tipo = when (it.tipo_desafio.lowercase()) {
                            "diario" -> TipoDesafio.DIARIO
                            "semanal" -> TipoDesafio.SEMANAL
                            else -> TipoDesafio.DIARIO
                        },
                        objetivosRelacionados = it.objetivos_relacionados
                    )
                }
            } catch (e: Exception) {
                _error.value = "Error cargando desafíos: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

package com.example.proyecto_droid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.model.TipoEjercicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegistroActividadUiState {
    object Loading : RegistroActividadUiState()
    data class Success(val registros: List<RegistroActividad>) : RegistroActividadUiState()
    data class Error(val message: String) : RegistroActividadUiState()
}

class RegistroActividadViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RegistroActividadUiState>(RegistroActividadUiState.Loading)
    val uiState: StateFlow<RegistroActividadUiState> = _uiState

    val isSaving = MutableStateFlow(false)
    val saveError = MutableStateFlow<String?>(null)
    val deleteError = MutableStateFlow<String?>(null)
    val isExporting = MutableStateFlow(false)
    val exportError = MutableStateFlow<String?>(null)
    val pdfFile = MutableStateFlow<String?>(null)

    init {
        cargarRegistros()
    }

    fun cargarRegistros() {
        viewModelScope.launch {
            // Simulación de carga de datos
            _uiState.value = RegistroActividadUiState.Success(emptyList())
        }
    }

    fun exportarPDF() {
        // Simulación de exportación
        isExporting.value = true
        viewModelScope.launch {
            // Simula un retardo
            kotlinx.coroutines.delay(1000)
            isExporting.value = false
            pdfFile.value = "ruta/del/archivo.pdf"
        }
    }

    fun confirmarEliminacion(id: Int) {
        // Simulación de eliminación
        deleteError.value = null
    }
} 
package com.example.proyecto_droid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.model.RegistroConsumo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegistroConsumoUiState {
    object Loading : RegistroConsumoUiState()
    data class Success(val registros: List<RegistroConsumo>) : RegistroConsumoUiState()
    data class Error(val message: String) : RegistroConsumoUiState()
}

class RegistroConsumoViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<RegistroConsumoUiState>(RegistroConsumoUiState.Loading)
    val uiState: StateFlow<RegistroConsumoUiState> = _uiState

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
            _uiState.value = RegistroConsumoUiState.Success(emptyList())
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
} 
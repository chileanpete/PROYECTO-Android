package com.example.proyecto_droid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.Desafio
import com.example.proyecto_droid.data.model.TipoDesafio
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DesafiosViewModel(application: Application) : AndroidViewModel(application) {
    
    private val apiService = UnifiedRetrofitClient.getApiService(application)
    
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
                val response = apiService.getDesafios()
                if (response.isSuccessful() && response.data != null) {
                    _desafios.value = response.data.data
                } else {
                    _error.value = response.getErrorMessage()
                }
            } catch (e: Exception) {
                _error.value = "Error cargando desafíos: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 
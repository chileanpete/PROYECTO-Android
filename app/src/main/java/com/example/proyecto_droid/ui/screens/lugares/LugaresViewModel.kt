package com.example.proyecto_droid.ui.screens.lugares

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.Lugar
import com.example.proyecto_droid.data.remote.services.LugarService
import kotlinx.coroutines.launch

sealed class LugaresUiState{
    data class Success(val lugares: List<Lugar>) : LugaresUiState()
    data class Error(val message: String) : LugaresUiState()
    object Loading : LugaresUiState()
    object Idle : LugaresUiState()
}

class LugaresViewModel(
    private val lugarService: LugarService
): ViewModel(){
    var lugaresUiState: LugaresUiState by mutableStateOf(LugaresUiState.Loading)
        private set

    fun resetUiState() {
        lugaresUiState = LugaresUiState.Idle
    }

    private fun getLugares(){
        viewModelScope.launch {
            lugaresUiState = LugaresUiState.Loading
            lugaresUiState = try {
                val listLugares = lugarService.getLugares()
                LugaresUiState.Success(listLugares)
            } catch (e: Exception) {
                LugaresUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshLugares() {
        getLugares()
    }

    fun addLugar() {
        viewModelScope.launch {
            lugaresUiState = LugaresUiState.Loading
            try {
                //lugarService.addLugar(Lugar())
                val updatedList = lugarService.getLugares()
                lugaresUiState = LugaresUiState.Success(updatedList)
            } catch (e: Exception) {
                lugaresUiState = LugaresUiState.Error(e.message ?: "Error al agregar plato")
            }
        }
    }
}
package com.example.proyecto_droid.ui.screens.platos

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.model.Plato
import com.example.proyecto_droid.data.remote.services.PlatoServices
import kotlinx.coroutines.launch

sealed class PlatosUiState{
    data class Success(val platos: List<Plato>) : PlatosUiState()
    data class Error(val message: String) : PlatosUiState()
    object Loading : PlatosUiState()
    object Idle : PlatosUiState()
}

class PlatosViewModel(
private val platoServices: PlatoServices
): ViewModel(){
    var platosUiState: PlatosUiState by mutableStateOf(PlatosUiState.Loading)
        private set

    fun resetUiState() {
        platosUiState = PlatosUiState.Idle
    }

    private fun getPlatos(){
        viewModelScope.launch {
            platosUiState = PlatosUiState.Loading
            platosUiState = try {
                val listPlatos = platoServices.getPlatos()
                PlatosUiState.Success(listPlatos)
            } catch (e: Exception) {
                PlatosUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun refreshPlatos() {
        getPlatos()
    }

//    fun addPlato(nombre: String, descripcion: String) {
//        viewModelScope.launch {
//            tareasUiState = TareasUiState.Loading
//            try {
//                tareaService.addTarea(Tarea(0,nombre,descripcion)) // Supón que tienes este método
//                val updatedList = tareaService.getTareas()
//                tareasUiState = TareasUiState.Success(updatedList)
//            } catch (e: Exception) {
//                tareasUiState = TareasUiState.Error(e.message ?: "Error al agregar tarea")
//            }
//        }
//    }
}
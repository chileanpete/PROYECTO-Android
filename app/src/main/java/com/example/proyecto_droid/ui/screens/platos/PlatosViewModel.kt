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

    fun addPlato(idLugar:Int,idCategoria:Int,nombrePlato:String,descripcionPlato: String, precioPlato: Double, caloriasPlato: Int, protePlato:Double,
                  carbPlato: Double, grasaPlato: Double, fibraPlato: Double, azucarPlato: Double, sodioPlato: Double,
                  disponibilidadPlato: Boolean, esVegetariano: Boolean, esVegano: Boolean, sinGluten: Boolean,
                  imagenUrl: String) {
        viewModelScope.launch {
            platosUiState = PlatosUiState.Loading
            try {
                platoServices.addPlato(Plato(0,idLugar,idCategoria,nombrePlato,descripcionPlato,precioPlato,
                    caloriasPlato,protePlato,carbPlato,grasaPlato,fibraPlato,azucarPlato,sodioPlato,disponibilidadPlato,
                    esVegetariano,esVegano,sinGluten,imagenUrl))
                val updatedList = platoServices.getPlatos()
                platosUiState = PlatosUiState.Success(updatedList)
            } catch (e: Exception) {
                platosUiState = PlatosUiState.Error(e.message ?: "Error al agregar plato")
            }
        }
    }
}
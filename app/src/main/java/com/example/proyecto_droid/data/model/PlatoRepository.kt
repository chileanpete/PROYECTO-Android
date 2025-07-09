package com.example.proyecto_droid.data.model

import kotlinx.coroutines.flow.Flow

interface PlatoRepository {
    fun getAllPlatoStream(): Flow<List<Plato>>
    fun getPlatoStream(id: Int): Flow<Plato?>
    suspend fun insertPlato(plato: Plato)
    suspend fun deletePlato(plato: Plato)
    suspend fun updatePlato(plato: Plato)
}
package com.example.proyecto_droid.data.model

import kotlinx.coroutines.flow.Flow

interface LugarRepository {
    fun getAllLugarStream(): Flow<List<Lugar>>
    fun getLugarStream(id: Int): Flow<Lugar?>
    suspend fun insertLugar(lugar: Lugar)
    suspend fun deleteLugar(lugar: Lugar)
    suspend fun updateLugar(lugar: Lugar)
}
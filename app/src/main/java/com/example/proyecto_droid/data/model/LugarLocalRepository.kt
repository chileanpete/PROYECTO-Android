package com.example.proyecto_droid.data.model

import kotlinx.coroutines.flow.Flow
import kotlin.text.insert

class LugarLocalRepository(private val lugarDao: LugarDao) : LugarRepository {
    override fun getAllLugarStream(): Flow<List<Lugar>> = lugarDao.getAllItems()

    override fun getLugarStream(id: Int): Flow<Lugar?> = lugarDao.getItem(id)

    override suspend fun insertLugar(lugar: Lugar) = lugarDao.insert(lugar)

    override suspend fun deleteLugar(lugar: Lugar) = lugarDao.delete(lugar)

    override suspend fun updateLugar(lugar: Lugar) = lugarDao.update(lugar)
}
package com.example.proyecto_droid.data.model

import kotlinx.coroutines.flow.Flow
import kotlin.text.insert

class PlatoLocalRepository(private val platoDao: PlatoDao) : PlatoRepository {
    override fun getAllPlatoStream(): Flow<List<Plato>> = platoDao.getAllItems()

    override fun getPlatoStream(id: Int): Flow<Plato?> = platoDao.getItem(id)

    override suspend fun insertPlato(plato: Plato) = platoDao.insert(plato)

    override suspend fun deletePlato(plato: Plato) = platoDao.delete(plato)

    override suspend fun updatePlato(plato: Plato) = platoDao.update(plato)
}
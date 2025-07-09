package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.local.AppDatabase
import com.example.proyecto_droid.data.local.entity.PlatoFavoritoEntity
import com.example.proyecto_droid.data.local.entity.TallerEntity

import kotlinx.coroutines.flow.Flow

class TallerRepository(context: Context){
    private val dao = AppDatabase.getDatabase(context).tallerDao()
    val talleres: Flow<List<TallerEntity>> = dao.getAll()

    fun getByFecha(fecha: String): Flow<List<TallerEntity>> = dao.getByFecha(fecha)
    suspend fun insertar(tallerEntity: TallerEntity) = dao.insert(tallerEntity)
    suspend fun eliminar(tallerEntity: TallerEntity) = dao.delete(tallerEntity)
}

class FavoritoRepository(context: Context){
    private val dao = AppDatabase.getDatabase(context).platofavoritoDao()
    val favoritos: Flow<List<PlatoFavoritoEntity>> = dao.getAll()

    suspend fun insertar(favoritoEntity: PlatoFavoritoEntity) = dao.insert(favoritoEntity)
    suspend fun eliminar(favoritoEntity: PlatoFavoritoEntity) = dao.delete(favoritoEntity)
}
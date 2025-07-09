package com.example.proyecto_droid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyecto_droid.data.local.entity.PlatoFavoritoEntity
import com.example.proyecto_droid.data.local.entity.TallerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TallerDao {
    @Query("SELECT * FROM talleres ORDER BY fecha_inicio ASC")
    fun getAll(): Flow<List<TallerEntity>>

    @Query("SELECT * FROM talleres WHERE fecha_inicio = :fecha")
    fun getByFecha(fecha: String): Flow<List<TallerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tallerEntity: TallerEntity)

    @Delete
    suspend fun delete(tallerEntity: TallerEntity)
}

@Dao
interface PlatoFavoritoDao{
    @Query("SELECT * FROM platos_favoritos")
    fun getAll(): Flow<List<PlatoFavoritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoritoEntity: PlatoFavoritoEntity)

    @Delete
    suspend fun delete(favoritoEntity: PlatoFavoritoEntity)
}
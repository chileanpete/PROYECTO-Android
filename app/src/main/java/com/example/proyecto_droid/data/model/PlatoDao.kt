package com.example.proyecto_droid.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlatoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plato: Plato)

    @Update
    suspend fun update(plato: Plato)

    @Delete
    suspend fun delete(plato: Plato)

    @Query("SELECT * FROM platos WHERE id = :id")
    fun getItem(id: Int): kotlinx.coroutines.flow.Flow<Plato>

    @Query("SELECT * FROM platos ORDER BY nombrePlato ASC")
    fun getAllItems(): kotlinx.coroutines.flow.Flow<List<Plato>>
}
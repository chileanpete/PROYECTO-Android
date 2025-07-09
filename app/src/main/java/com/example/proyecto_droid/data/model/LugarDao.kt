package com.example.proyecto_droid.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LugarDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(lugar: Lugar)

    @Update
    suspend fun update(lugar: Lugar)

    @Delete
    suspend fun delete(lugar: Lugar)

    @Query("SELECT * FROM lugares WHERE id = :id")
    fun getItem(id: Int): kotlinx.coroutines.flow.Flow<Lugar>

    @Query("SELECT * FROM lugares ORDER BY nombre ASC")
    fun getAllItems(): kotlinx.coroutines.flow.Flow<List<Lugar>>
}
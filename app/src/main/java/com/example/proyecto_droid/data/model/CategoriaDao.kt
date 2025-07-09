package com.example.proyecto_droid.data.model
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(categoria: Categoria)

    @Update
    suspend fun update(categoria: Categoria)

    @Delete
    suspend fun delete(categoria: Categoria)

    @Query("SELECT * FROM categorias WHERE id_categoria = :id")
    fun getItem(id: Int): kotlinx.coroutines.flow.Flow<Categoria>

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAllItems(): kotlinx.coroutines.flow.Flow<List<Categoria>>
}
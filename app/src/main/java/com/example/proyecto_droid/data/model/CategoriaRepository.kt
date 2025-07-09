package com.example.proyecto_droid.data.model

import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    fun getAllCategoriaStream(): Flow<List<Categoria>>
    fun getCategoriaStream(id: Int): Flow<Categoria?>
    suspend fun insertCategoria(categoria: Categoria)
    suspend fun deleteCategoria(categoria: Categoria)
    suspend fun updateCategoria(categoria: Categoria)
}
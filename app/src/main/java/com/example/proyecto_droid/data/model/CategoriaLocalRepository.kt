package com.example.proyecto_droid.data.model

import kotlinx.coroutines.flow.Flow
import kotlin.text.insert

class CategoriaLocalRepository(private val categoriaDao: CategoriaDao) : CategoriaRepository {
    override fun getAllCategoriaStream(): Flow<List<Categoria>> = categoriaDao.getAllItems()

    override fun getCategoriaStream(id: Int): Flow<Categoria?> = categoriaDao.getItem(id)

    override suspend fun insertCategoria(categoria: Categoria) = categoriaDao.insert(categoria)

    override suspend fun deleteCategoria(categoria: Categoria) = categoriaDao.delete(categoria)

    override suspend fun updateCategoria(categoria: Categoria) = categoriaDao.update(categoria)
}
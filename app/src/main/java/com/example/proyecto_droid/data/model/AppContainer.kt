package com.example.proyecto_droid.data.model

import android.content.Context

interface AppContainer {
    val categoriaRepository: CategoriaRepository
    val lugarRepository: LugarRepository
    val platoRepository: PlatoRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val database by lazy { AppDatabase.getDatabase(context) }

    override val categoriaRepository: CategoriaRepository by lazy {
        CategoriaLocalRepository(database.categoriaDao())
    }
    override val lugarRepository: LugarRepository by lazy {
        LugarLocalRepository(database.lugarDao())
    }
    override val platoRepository: PlatoRepository by lazy {
        PlatoLocalRepository(database.platoDao())
    }


}

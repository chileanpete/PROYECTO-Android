package com.example.proyecto_droid.ui.viewmodel

import android.app.Application
import com.example.proyecto_droid.data.local.entity.TallerEntity
import com.example.proyecto_droid.data.repository.TallerRepository
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_droid.data.local.entity.PlatoFavoritoEntity
import com.example.proyecto_droid.data.repository.FavoritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TallerViewModel(application: Application): AndroidViewModel(application){
    private val repo = TallerRepository(application)
    val talleres: StateFlow<List<TallerEntity>> = repo.talleres.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun getByFecha(fecha: String): Flow<List<TallerEntity>> = repo.getByFecha(fecha)

    fun addTaller(t: TallerEntity) = viewModelScope.launch {
        repo.insertar(t)
    }
}

class FavoritoViewModel(application: Application): AndroidViewModel(application){
    private val repo = FavoritoRepository(application)
    val favoritos: StateFlow<List<PlatoFavoritoEntity>> = repo.favoritos.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun add(f: PlatoFavoritoEntity) = viewModelScope.launch {
        repo.insertar(f)
    }

    fun remove(f: PlatoFavoritoEntity) = viewModelScope.launch {
        repo.eliminar(f)
    }
}


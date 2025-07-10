package com.example.proyecto_droid.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient

/**
 * Factory para PlatosViewModel
 */
class PlatosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlatosViewModel::class.java)) {
            val apiService = UnifiedRetrofitClient.getApiService(context)
            return PlatosViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory para LugaresViewModel
 */
class LugaresViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LugaresViewModel::class.java)) {
            val apiService = UnifiedRetrofitClient.getApiService(context)
            return LugaresViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Factory para FavoritosMenuViewModel
 */
class FavoritosMenuViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritosMenuViewModel::class.java)) {
            val apiService = UnifiedRetrofitClient.getApiService(context)
            return FavoritosMenuViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
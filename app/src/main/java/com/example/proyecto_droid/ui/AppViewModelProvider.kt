package com.example.proyecto_droid.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_droid.Proyecto_droid
import com.example.proyecto_droid.data.model.AppContainer
import com.example.proyecto_droid.ui.screens.platos.LocalPlatoVeiwModel
import com.example.proyecto_droid.ui.screens.lugares.LocalLugarViewModel
import com.example.proyecto_droid.ui.screens.categorias.CategoriaViewModel

object AppViewModelProvider {
    lateinit var appContainer: AppContainer

    val Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(CategoriaViewModel::class.java) -> {
                    CategoriaViewModel(appContainer.categoriaRepository) as T
                }
                modelClass.isAssignableFrom(LocalLugarViewModel::class.java) -> {
                    LocalLugarViewModel(appContainer.lugarRepository) as T
                }
                modelClass.isAssignableFrom(LocalPlatoVeiwModel::class.java) -> {
                    LocalPlatoVeiwModel(appContainer.platoRepository) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.CreationExtras
//import androidx.lifecycle.viewmodel.initializer
//import androidx.lifecycle.viewmodel.viewModelFactory
//import com.example.proyecto_droid.Proyecto_droid
//import com.example.proyecto_droid.ui.screens.platos.PlatosViewModel
//import com.example.proyecto_droid.ui.screens.auth.AuthViewModel
//import com.example.proyecto_droid.ui.screens.lugares.LugaresViewModel
//
//object AppViewModelProvider {
//    val Factory = viewModelFactory {
//        initializer {
//            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Proyecto_droid)
//            LugaresViewModel(
//                lugarService = application.container.lugarApiService
//            )
//        }
//        initializer {
//            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Proyecto_droid)
//            PlatosViewModel(
//                platoServices = application.container.platoApiService
//            )
//        }
//        initializer {
//            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Proyecto_droid)
//            AuthViewModel(
//                authService = application.container.authApiService,
//                context = application.applicationContext
//            )
//        }
//    }
//}
//
//fun CreationExtras.App(): Proyecto_droid =
//    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Proyecto_droid)
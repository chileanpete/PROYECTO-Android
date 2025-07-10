//package com.example.proyecto_droid.ui
//
//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.proyecto_droid.Proyecto_droid
//import com.example.proyecto_droid.ui.screens.categorias.CategoriaViewModel
//import com.example.proyecto_droid.ui.screens.lugares.LocalLugarViewModel
//import com.example.proyecto_droid.ui.screens.platos.LocalPlatoVeiwModel
//
//class ViewModelProvider(private val application: Application) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val container = (application as Proyecto_droid).container
//        return when {
//            modelClass.isAssignableFrom(CategoriaViewModel::class.java) -> {
//                CategoriaViewModel(container.categoriaRepository) as T
//            }
//            modelClass.isAssignableFrom(LocalLugarViewModel::class.java) -> {
//                LocalLugarViewModel(container.lugarRepository) as T
//            }
//            modelClass.isAssignableFrom(LocalPlatoVeiwModel::class.java) -> {
//                LocalPlatoVeiwModel(container.platoRepository) as T
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//}
package com.example.proyecto_droid.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.proyecto_droid.Proyecto_droid
import com.example.proyecto_droid.ui.screens.platos.PlatosViewModel
import com.example.proyecto_droid.ui.screens.auth.AuthViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            PlatosViewModel(
                platoServices= App().container.platoApiService

            )
        }
        initializer {
            AuthViewModel(
                authService = App().container.authApiService,
                context = App().baseContext
            )
        }
    }
}

fun CreationExtras.App(): Proyecto_droid =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Proyecto_droid)
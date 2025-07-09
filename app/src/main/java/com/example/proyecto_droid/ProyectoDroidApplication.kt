package com.example.proyecto_droid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application principal de la aplicación
 * Configurada para usar Hilt para inyección de dependencias
 */
@HiltAndroidApp
class ProyectoDroidApplication : Application() 
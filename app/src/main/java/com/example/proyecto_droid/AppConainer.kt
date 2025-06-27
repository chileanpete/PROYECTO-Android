package com.example.proyecto_droid

import android.app.Application
import com.example.proyecto_droid.data.AppContainer
import com.example.proyecto_droid.data.AppDataContainer

class Proyecto_droid : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
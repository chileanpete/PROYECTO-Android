package com.example.proyecto_droid.data

import android.content.Context
import com.example.proyecto_droid.data.remote.ApiClient
import com.example.proyecto_droid.data.remote.services.PlatoServices

interface AppContainer{
    val platoApiService: PlatoServices
}

class AppDataContainer(private val context: Context) :com.example.proyecto_droid.data.AppContainer {
    override val platoApiService: PlatoServices by lazy {
        ApiClient.create(context).create(PlatoServices::class.java)
    }

}


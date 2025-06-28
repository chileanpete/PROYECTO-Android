package com.example.proyecto_droid.data

import android.content.Context
import com.example.proyecto_droid.data.remote.ApiClient
import com.example.proyecto_droid.data.remote.services.PlatoServices
import com.example.proyecto_droid.data.remote.services.AuthService
import com.example.proyecto_droid.data.remote.services.LugarService
import retrofit2.create

interface AppContainer{
    val platoApiService: PlatoServices
    val lugarApiService:LugarService
    val authApiService: AuthService

}

class AppDataContainer(private val context: Context) :com.example.proyecto_droid.data.AppContainer {
    override val platoApiService: PlatoServices by lazy {
        ApiClient.create(context).create(PlatoServices::class.java)
    }
    override val lugarApiService: LugarService by lazy {
        ApiClient.create(context).create(LugarService::class.java)
    }
    override val authApiService: AuthService by lazy {
        ApiClient.create(context).create(AuthService::class.java)
    }

}


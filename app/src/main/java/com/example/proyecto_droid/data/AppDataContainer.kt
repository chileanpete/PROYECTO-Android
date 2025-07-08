package com.example.proyecto_droid.data

import android.content.Context
import com.example.proyecto_droid.data.remote.ApiClient
import com.example.proyecto_droid.data.remote.ApiService
import com.example.proyecto_droid.data.remote.services.PlatoServices
import com.example.proyecto_droid.data.remote.services.AuthService
import com.example.proyecto_droid.data.remote.services.LugarService
import retrofit2.Retrofit
import retrofit2.create

interface AppContainer{
    val platoApiService: PlatoServices
    val lugarApiService:LugarService
    val authApiService: AuthService

}

class AppDataContainer(private val context: Context) :com.example.proyecto_droid.data.AppContainer {
    private val retrofit: Retrofit by lazy {
        ApiClient.createRetrofit(context)
    }

    // 2. Implementa las propiedades correctamente
    override val platoApiService: PlatoServices by lazy {
        retrofit.create<PlatoServices>()
    }

    override val lugarApiService: LugarService by lazy {
        retrofit.create<LugarService>()
    }

    override val authApiService: AuthService by lazy {
        retrofit.create<AuthService>()
    }
}


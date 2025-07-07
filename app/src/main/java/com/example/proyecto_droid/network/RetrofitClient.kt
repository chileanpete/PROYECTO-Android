package com.example.proyecto_droid.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.proyecto_droid.util.Constants

object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.6:8000/api/"  // Para emulador Android Studio
    // Si pruebas en un celular real, usa tu IP local (ej: http://192.168.0.100:8000/api/)

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("${Constants.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

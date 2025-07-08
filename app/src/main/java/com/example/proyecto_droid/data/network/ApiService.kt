package com.example.proyecto_droid.data.network

import com.example.proyecto_droid.data.model.DesafioSugeridoResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/desafios")
    suspend fun getDesafios(): List<DesafioSugeridoResponse>
} 
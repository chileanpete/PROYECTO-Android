package com.example.proyecto_droid.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import com.example.proyecto_droid.model.TestResponse
import com.example.proyecto_droid.model.UserResponse


interface ApiService {
    @GET("test") // Ruta RELATIVA a BASE_URL
    suspend fun testEndpoint(): Response<TestResponse> // Usa tu modelo de datos

    // Ejemplo con autenticación:
    @GET("user")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserResponse>
}
package com.example.proyecto_droid.data.remote.services

import com.example.proyecto_droid.data.model.Lugar
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LugarService {
    @GET("api/lugares")
    suspend fun getLugares(): List<Lugar>

    @POST("api/lugares")
    suspend fun addLugar(@Body lugar: Lugar): Lugar
}
package com.example.proyecto_droid.data

import com.example.proyecto_droid.data.model.Taller_Recreativo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TallerService {
    @GET("api/talleres_recreativos")
    suspend fun getTalleres(): List<Taller_Recreativo>

    @POST("api/talleres_recreativos")
    suspend fun addTaller(@Body taller: Taller_Recreativo): Taller_Recreativo
}
package com.example.proyecto_droid.data.remote.services

import com.example.proyecto_droid.data.model.Plato
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PlatoServices {
    @GET("api/platos")
    suspend fun getPlatos(): List<Plato>

    @POST("api/platos")
    suspend fun addPlato(@Body tarea: Plato): Plato
}
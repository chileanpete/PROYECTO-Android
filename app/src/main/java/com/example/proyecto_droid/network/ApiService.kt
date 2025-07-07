package com.example.proyecto_droid.network

import com.example.proyecto_droid.model.DesafioSugeridoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.proyecto_droid.model.DesafioApiModel

interface ApiService {
    @GET("usuario/{id}/desafios-sugeridos")
    fun getDesafiosSugeridos(@Path("id") userId: Int): Call<DesafioSugeridoResponse>

    @GET("desafios")
    suspend fun getDesafios(): List<DesafioApiModel>


}
package com.example.proyecto_droid.data.network

import com.example.proyecto_droid.data.model.DesafioSugeridoResponse
import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api/desafios")
    suspend fun getDesafios(): List<DesafioSugeridoResponse>

    @POST("api/actividad-fisica/")
    suspend fun crearRegistroActividad(@Body registro: Map<String, @JvmSuppressWildcards Any?>): retrofit2.Response<Any>

    @GET("api/actividad-fisica")
    suspend fun getRegistrosActividadFisica(
        @Query("id_usuario") idUsuario: Int
    ): Response<ApiResponse<List<RegistroActividad>>>

    @GET("api/exportacion/actividad-pdf")
    suspend fun exportarPDFActividad(@Query("id_usuario") idUsuario: Int): Response<com.example.proyecto_droid.data.network.services.ApiResponse<com.example.proyecto_droid.data.network.services.ExportarPDFResponse>>
} 
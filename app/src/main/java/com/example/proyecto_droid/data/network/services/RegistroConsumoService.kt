package com.example.proyecto_droid.data.network.services

import com.example.proyecto_droid.model.RegistroConsumo
import com.example.proyecto_droid.model.Plato
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

data class PaginatedPlatoResponse(
    @SerializedName("current_page")
    val current_page: Int,
    @SerializedName("data")
    val data: List<Plato>,
    @SerializedName("first_page_url")
    val first_page_url: String?,
    @SerializedName("from")
    val from: Int?,
    @SerializedName("last_page")
    val last_page: Int?,
    @SerializedName("last_page_url")
    val last_page_url: String?,
    @SerializedName("next_page_url")
    val next_page_url: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("per_page")
    val per_page: Int?,
    @SerializedName("prev_page_url")
    val prev_page_url: String?,
    @SerializedName("to")
    val to: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("links")
    val links: List<Any>? = null
)

data class PaginatedRegistroConsumoResponse(
    @SerializedName("current_page")
    val current_page: Int,
    @SerializedName("data")
    val data: List<RegistroConsumo>,
    @SerializedName("first_page_url")
    val first_page_url: String?,
    @SerializedName("from")
    val from: Int?,
    @SerializedName("last_page")
    val last_page: Int?,
    @SerializedName("last_page_url")
    val last_page_url: String?,
    @SerializedName("next_page_url")
    val next_page_url: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("per_page")
    val per_page: Int?,
    @SerializedName("prev_page_url")
    val prev_page_url: String?,
    @SerializedName("to")
    val to: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("links")
    val links: List<Any>? = null
)

interface RegistroConsumoService {
    // Define aquí los métodos de la API si los necesitas
    @POST("api/registro-consumo")
    suspend fun crearRegistroConsumo(@Body registro: CrearRegistroConsumoRequest): Response<ApiResponse<RegistroConsumo>>
    
    @GET("api/registro-consumo")
    suspend fun getRegistrosConsumo(@Query("id_usuario") idUsuario: Int): Response<ApiResponse<PaginatedRegistroConsumoResponse>>
    
    @GET("api/platos")
    suspend fun getPlatos(): Response<ApiResponse<PaginatedPlatoResponse>>

    @GET("api/exportacion/consumo-pdf")
    suspend fun exportarPDF(@Query("id_usuario") idUsuario: Int): Response<ApiResponse<ExportarPDFResponse>>

    @GET("api/actividad-fisica/exportar-pdf")
    suspend fun exportarPDFActividad(): Response<ApiResponse<ExportarPDFResponse>>
}

data class CrearRegistroConsumoRequest(
    val id_plato: Int,
    val fecha_consumo: String,
    val hora_consumo: String,
    val porciones: Double,
    val valoracion: Int? = null,
    val comentario: String? = null,
    val calorias_totales: Int? = null
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
) 

data class ExportarPDFResponse(
    val filename: String,
    val content: String // base64
) 
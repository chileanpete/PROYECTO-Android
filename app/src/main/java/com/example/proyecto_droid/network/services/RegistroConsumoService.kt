package com.example.proyecto_droid.network.services

import com.example.proyecto_droid.model.RegistroConsumo
import com.example.proyecto_droid.model.Plato
import com.example.proyecto_droid.model.CategoriaComida
import com.example.proyecto_droid.model.LugarComida
import retrofit2.Response
import retrofit2.http.*

interface RegistroConsumoService {
    
    @GET("registro-consumo")
    suspend fun getRegistrosConsumo(): Response<ApiResponse<List<RegistroConsumo>>>
    
    @GET("registro-consumo/{id}")
    suspend fun getRegistroConsumo(@Path("id") id: Int): Response<ApiResponse<RegistroConsumo>>
    
    @POST("registro-consumo")
    suspend fun crearRegistroConsumo(@Body registro: CrearRegistroConsumoRequest): Response<ApiResponse<RegistroConsumo>>
    
    @PUT("registro-consumo/{id}")
    suspend fun actualizarRegistroConsumo(
        @Path("id") id: Int,
        @Body registro: ActualizarRegistroConsumoRequest
    ): Response<ApiResponse<RegistroConsumo>>
    
    @DELETE("registro-consumo/{id}")
    suspend fun eliminarRegistroConsumo(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    @GET("registro-consumo/exportar/pdf")
    suspend fun exportarPDF(): Response<ApiResponse<ExportarPDFResponse>>
    
    @GET("registro-consumo/estadisticas")
    suspend fun getEstadisticas(): Response<ApiResponse<EstadisticasConsumo>>
    
    @GET("platos")
    suspend fun getPlatos(
        @Query("categoria") categoria: Int? = null,
        @Query("lugar") lugar: Int? = null,
        @Query("vegetariano") vegetariano: Boolean? = null,
        @Query("vegano") vegano: Boolean? = null,
        @Query("sin_gluten") sinGluten: Boolean? = null
    ): Response<ApiResponse<List<Plato>>>
    
    @GET("platos/{id}")
    suspend fun getPlato(@Path("id") id: Int): Response<ApiResponse<Plato>>
    
    @GET("platos/categorias")
    suspend fun getCategorias(): Response<ApiResponse<List<CategoriaComida>>>
    
    @GET("platos/lugares")
    suspend fun getLugares(): Response<ApiResponse<List<LugarComida>>>
    
    @GET("platos/buscar")
    suspend fun buscarPlatos(@Query("q") query: String): Response<ApiResponse<List<Plato>>>
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

data class ActualizarRegistroConsumoRequest(
    val id_plato: Int? = null,
    val fecha_consumo: String? = null,
    val hora_consumo: String? = null,
    val porciones: Double? = null,
    val valoracion: Int? = null,
    val comentario: String? = null
)

data class EstadisticasConsumo(
    val hoy: EstadisticasPeriodo,
    val semana: EstadisticasPeriodo,
    val mes: EstadisticasPeriodo
)

data class EstadisticasPeriodo(
    val total_calorias: Int,
    val total_registros: Int
)

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
)

data class ExportarPDFResponse(
    val filename: String,
    val content: String
) 
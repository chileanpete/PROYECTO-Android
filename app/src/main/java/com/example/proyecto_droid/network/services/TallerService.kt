package com.example.proyecto_droid.network.services

import com.example.proyecto_droid.model.TallerRecreativo
import com.example.proyecto_droid.model.InscripcionTaller
import retrofit2.Response
import retrofit2.http.*

interface TallerService {
    
    @GET("inscripciones-talleres")
    suspend fun getInscripcionesTalleres(): Response<ApiResponse<List<InscripcionTaller>>>
    
    @GET("inscripciones-talleres/{id}")
    suspend fun getInscripcionTaller(@Path("id") id: Int): Response<ApiResponse<InscripcionTaller>>
    
    @POST("inscripciones-talleres")
    suspend fun crearInscripcionTaller(@Body inscripcion: CrearInscripcionTallerRequest): Response<ApiResponse<InscripcionTaller>>
    
    @PUT("inscripciones-talleres/{id}")
    suspend fun actualizarInscripcionTaller(
        @Path("id") id: Int,
        @Body inscripcion: ActualizarInscripcionTallerRequest
    ): Response<ApiResponse<InscripcionTaller>>
    
    @DELETE("inscripciones-talleres/{id}")
    suspend fun eliminarInscripcionTaller(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    @GET("inscripciones-talleres/talleres-disponibles")
    suspend fun getTalleresDisponibles(): Response<ApiResponse<List<TallerRecreativo>>>
    
    @GET("inscripciones-talleres/estadisticas")
    suspend fun getEstadisticas(): Response<ApiResponse<EstadisticasTalleres>>
}

data class CrearInscripcionTallerRequest(
    val id_taller: Int,
    val calificacion: Int? = null,
    val comentario: String? = null
)

data class ActualizarInscripcionTallerRequest(
    val estado: String? = null,
    val calificacion: Int? = null,
    val comentario: String? = null
)

data class EstadisticasTalleres(
    val total_inscripciones: Int,
    val talleres_completados: Int,
    val talleres_activos: Int,
    val puntos_totales: Int,
    val calificacion_promedio: Double?
) 
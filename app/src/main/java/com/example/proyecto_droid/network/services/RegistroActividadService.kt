package com.example.proyecto_droid.network.services

import com.example.proyecto_droid.model.RegistroActividad
import com.example.proyecto_droid.model.TipoEjercicio
import com.example.proyecto_droid.model.RutinaEjercicio
import retrofit2.Response
import retrofit2.http.*

interface RegistroActividadService {
    
    @GET("registro-actividad")
    suspend fun getRegistrosActividad(): Response<ApiResponse<List<RegistroActividad>>>
    
    @GET("registro-actividad/{id}")
    suspend fun getRegistroActividad(@Path("id") id: Int): Response<ApiResponse<RegistroActividad>>
    
    @POST("registro-actividad")
    suspend fun crearRegistroActividad(@Body registro: CrearRegistroActividadRequest): Response<ApiResponse<RegistroActividad>>
    
    @PUT("registro-actividad/{id}")
    suspend fun actualizarRegistroActividad(
        @Path("id") id: Int,
        @Body registro: ActualizarRegistroActividadRequest
    ): Response<ApiResponse<RegistroActividad>>
    
    @DELETE("registro-actividad/{id}")
    suspend fun eliminarRegistroActividad(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    @GET("registro-actividad/estadisticas")
    suspend fun getEstadisticas(): Response<ApiResponse<EstadisticasActividad>>
    
    @GET("registro-actividad/tipos-ejercicio")
    suspend fun getTiposEjercicio(): Response<ApiResponse<List<TipoEjercicio>>>
    
    @GET("registro-actividad/rutinas-ejercicio")
    suspend fun getRutinasEjercicio(): Response<ApiResponse<List<RutinaEjercicio>>>
}

data class CrearRegistroActividadRequest(
    val id_tipo_ejercicio: Int? = null,
    val id_rutina: Int? = null,
    val fecha_actividad: String,
    val hora_inicio: String,
    val hora_fin: String,
    val duracion_minutos: Int,
    val calorias_quemadas: Int,
    val intensidad: Int,
    val comentario: String? = null,
    val completada: Boolean = true
)

data class ActualizarRegistroActividadRequest(
    val id_tipo_ejercicio: Int? = null,
    val id_rutina: Int? = null,
    val fecha_actividad: String? = null,
    val hora_inicio: String? = null,
    val hora_fin: String? = null,
    val duracion_minutos: Int? = null,
    val calorias_quemadas: Int? = null,
    val intensidad: Int? = null,
    val comentario: String? = null,
    val completada: Boolean? = null
)

data class EstadisticasActividad(
    val hoy: EstadisticasActividadPeriodo,
    val semana: EstadisticasActividadPeriodo,
    val mes: EstadisticasActividadPeriodo
)

data class EstadisticasActividadPeriodo(
    val total_calorias_quemadas: Int,
    val total_minutos: Int,
    val total_actividades: Int
) 
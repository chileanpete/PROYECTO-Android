package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.local.AuthManager
import com.example.proyecto_droid.data.model.*
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

/**
 * Repositorio unificado para operaciones de actividad física.
 * Maneja la integración con la API remota para registros de actividad.
 */
class UnifiedActivityRepository(private val context: Context) {
    
    private val authManager = AuthManager(context)
    private val apiService = UnifiedRetrofitClient.getApiService(context)
    
    /**
     * Obtiene todas las actividades físicas del usuario
     */
    suspend fun getActividades(userId: Int? = null, page: Int? = null): Result<PaginatedResponse<RegistroActividad>> {
        return try {
            val response = apiService.getActividadesFisicas(userId, page)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene una actividad física específica por ID
     */
    suspend fun getActividad(id: Int): Result<RegistroActividad> {
        return try {
            val response = apiService.getActividadFisica(id)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Crea un nuevo registro de actividad física
     */
    suspend fun createActividad(registro: RegistroActividad): Result<RegistroActividad> {
        return try {
            val response = apiService.createActividadFisica(registro)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Actualiza un registro de actividad física existente
     */
    suspend fun updateActividad(id: Int, registro: RegistroActividad): Result<RegistroActividad> {
        return try {
            val response = apiService.updateActividadFisica(id, registro)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Elimina un registro de actividad física
     */
    suspend fun deleteActividad(id: Int): Result<Unit> {
        return try {
            val response = apiService.deleteActividadFisica(id)
            
            if (response.isSuccessful()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene estadísticas de actividad física del usuario
     */
    suspend fun getEstadisticasActividad(userId: Int): Result<Map<String, Any>> {
        return try {
            val response = apiService.getEstadisticasActividad(userId)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene actividades por rango de fechas
     */
    suspend fun getActividadesPorFecha(userId: Int, fechaInicio: String, fechaFin: String): Result<List<RegistroActividad>> {
        return try {
            val dateRange = mapOf(
                "fecha_inicio" to fechaInicio,
                "fecha_fin" to fechaFin
            )
            val response = apiService.getActividadesPorFecha(userId, dateRange)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Marca una actividad como completada
     */
    suspend fun marcarActividadCompletada(id: Int): Result<RegistroActividad> {
        return try {
            val response = apiService.marcarActividadCompletada(id)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Exporta las actividades a PDF
     */
    suspend fun exportarPDFActividad(): Result<ExportResponse> {
        return try {
            val response = apiService.exportarPDFActividad()
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene los tipos de ejercicio disponibles
     */
    suspend fun getTiposEjercicio(): Result<List<TipoEjercicio>> {
        return try {
            val response = apiService.getTiposEjercicio()
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene las actividades del usuario actual
     */
    suspend fun getActividadesUsuarioActual(page: Int? = null): Result<PaginatedResponse<RegistroActividad>> {
        return try {
            val userId = authManager.userId.first()
            
            if (userId == null || userId == 0) {
                return Result.failure(Exception("Usuario no autenticado"))
            }
            
            getActividades(userId, page)
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener actividades: ${e.message}"))
        }
    }
    
    /**
     * Maneja las excepciones HTTP de manera consistente
     */
    private fun <T> handleHttpException(e: HttpException): Result<T> {
        val errorMessage = when (e.code()) {
            401 -> "Sesión expirada. Por favor, inicia sesión nuevamente"
            403 -> "No tienes permisos para realizar esta acción"
            404 -> "Recurso no encontrado"
            422 -> "Los datos proporcionados no son válidos"
            500 -> "Error interno del servidor"
            else -> "Error en el servidor: ${e.message()}"
        }
        return Result.failure(Exception(errorMessage))
    }
} 
package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de respuesta unificado para todas las APIs del backend Laravel.
 * Proporciona una estructura consistente para el manejo de respuestas y errores.
 */
data class UnifiedApiResponse<T>(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("data")
    val data: T? = null,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("error")
    val error: String? = null,
    
    @SerializedName("errors") 
    val errors: Map<String, List<String>>? = null,
    
    @SerializedName("status_code")
    val statusCode: Int? = null,
    
    @SerializedName("timestamp")
    val timestamp: String? = null
) {
    /**
     * Verifica si la respuesta fue exitosa
     */
    fun isSuccessful(): Boolean = success && data != null
    
    /**
     * Obtiene el mensaje de error apropiado
     */
    fun getErrorMessage(): String {
        return when {
            !error.isNullOrBlank() -> error
            !message.isNullOrBlank() && !success -> message
            errors?.isNotEmpty() == true -> {
                errors.values.flatten().firstOrNull() ?: "Error desconocido"
            }
            else -> "Error desconocido"
        }
    }
    
    /**
     * Obtiene todos los errores de validación
     */
    fun getValidationErrors(): Map<String, List<String>> {
        return errors ?: emptyMap()
    }
}

/**
 * Resultado de operación simplificado para uso interno
 */
sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String, val code: Int? = null) : ApiResult<T>()
    data class Loading<T>(val isLoading: Boolean = true) : ApiResult<T>()
}

/**
 * Extensión para convertir UnifiedApiResponse a ApiResult
 */
fun <T> UnifiedApiResponse<T>.toApiResult(): ApiResult<T> {
    return if (isSuccessful() && data != null) {
        ApiResult.Success(data)
    } else {
        ApiResult.Error(getErrorMessage(), statusCode)
    }
}



/**
 * Modelo para respuestas de paginación
 */
data class PaginatedResponse<T>(
    @SerializedName("data")
    val data: List<T>,
    
    @SerializedName("current_page")
    val currentPage: Int,
    
    @SerializedName("last_page")
    val lastPage: Int,
    
    @SerializedName("per_page")
    val perPage: Int,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("from")
    val from: Int? = null,
    
    @SerializedName("to")
    val to: Int? = null
)

/**
 * Modelo para exportación de PDFs
 */
data class ExportResponse(
    @SerializedName("filename")
    val filename: String,
    
    @SerializedName("download_url")
    val downloadUrl: String,
    
    @SerializedName("file_size")
    val fileSize: Long? = null,
    
    @SerializedName("expires_at")
    val expiresAt: String? = null
) 
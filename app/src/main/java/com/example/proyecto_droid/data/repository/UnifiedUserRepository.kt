package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.local.AuthManager
import com.example.proyecto_droid.data.local.SessionManager
import com.example.proyecto_droid.data.model.*
import com.example.proyecto_droid.data.network.UnifiedRetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

/**
 * Repositorio unificado para operaciones de usuario.
 * Maneja la integración entre API remota, autenticación local y sesión de usuario.
 */
class UnifiedUserRepository(private val context: Context) {
    
    private val sessionManager = SessionManager(context)
    private val authManager = AuthManager(context)
    private val apiService = UnifiedRetrofitClient.getApiService(context)
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    suspend fun registerUser(user: User): Result<User> {
        return try {
            val registerRequest = RegisterRequest(
                email = user.email,
                password = user.passwordHash,
                nombre = user.nombre,
                apellidos = user.apellidos,
                fechaNacimiento = user.fechaNacimiento,
                genero = user.genero,
                alturaCm = user.alturaCm,
                pesoKg = user.pesoKg,
                nivelActividad = user.nivelActividad,
                objetivoPrincipal = user.objetivoPrincipal,
                preferenciasAlimentarias = user.preferenciasAlimentarias,
                alergias = user.alergias
            )
            
            val response = apiService.register(registerRequest)
            
            if (response.isSuccessful() && response.data != null) {
                val registeredUser = response.data.usuario
                val token = response.data.token
                
                // Guardar datos de autenticación
                authManager.saveAuthData(token, registeredUser.id ?: 0, registeredUser.email)
                
                // Guardar sesión local
                sessionManager.saveUserSession(
                    registeredUser.email, 
                    "${registeredUser.nombre} ${registeredUser.apellidos}", 
                    registeredUser.id?.toString() ?: ""
                )
                
                Result.success(registeredUser)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                422 -> "Los datos proporcionados no son válidos"
                409 -> "El email ya está registrado"
                else -> "Error en el servidor: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Inicia sesión de usuario
     */
    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response = apiService.login(loginRequest)
            
            if (response.isSuccessful() && response.data != null) {
                val user = response.data.usuario
                val token = response.data.token
                
                if (user.id == null) {
                    return Result.failure(Exception("El usuario no tiene un ID válido"))
                }
                
                // Guardar datos de autenticación
                authManager.saveAuthData(token, user.id, user.email)
                
                // Guardar sesión local
                sessionManager.saveUserSession(
                    email, 
                    "${user.nombre} ${user.apellidos}", 
                    user.id.toString()
                )
                
                Result.success(user)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Credenciales inválidas"
                404 -> "Usuario no encontrado"
                else -> "Error en el servidor: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    suspend fun logoutUser() {
        sessionManager.clearUserSession()
        authManager.clearAuthData()
    }
    
    /**
     * Verifica si el usuario está logueado
     */
    fun isUserLoggedIn(): Flow<Boolean> {
        return sessionManager.isLoggedIn
    }
    
    /**
     * Obtiene el email del usuario actual
     */
    fun getCurrentUserEmail(): Flow<String?> {
        return sessionManager.userEmail
    }
    
    /**
     * Obtiene el nombre del usuario actual
     */
    fun getCurrentUserName(): Flow<String?> {
        return sessionManager.userName
    }
    
    /**
     * Obtiene el perfil completo del usuario actual
     */
    suspend fun getCurrentUserProfile(): Result<User> {
        return try {
            val token = authManager.authToken.first()
            val userId = authManager.userId.first()
            
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Token de usuario no disponible"))
            }
            if (userId == null || userId == 0) {
                return Result.failure(Exception("ID de usuario no disponible o inválido"))
            }
            
            val response = apiService.getUser(userId)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Sesión expirada. Por favor, inicia sesión nuevamente"
                404 -> "Usuario no encontrado"
                else -> "Error en el servidor: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Actualiza el perfil del usuario
     */
    suspend fun updateUser(user: User): Result<User> {
        return try {
            val token = authManager.authToken.first()
            val userId = authManager.userId.first()
            
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Token de usuario no disponible"))
            }
            if (userId == null || userId == 0) {
                return Result.failure(Exception("ID de usuario no disponible o inválido"))
            }
            
            val response = apiService.updateUser(userId, user)
            
            if (response.isSuccessful() && response.data != null) {
                val updatedUser = response.data
                sessionManager.updateUserName("${updatedUser.nombre} ${updatedUser.apellidos}")
                Result.success(updatedUser)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Sesión expirada. Por favor, inicia sesión nuevamente"
                422 -> "Los datos proporcionados no son válidos"
                else -> "Error en el servidor: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Elimina el usuario actual
     */
    suspend fun deleteUser(): Result<Unit> {
        return try {
            val token = authManager.authToken.first()
            val userId = authManager.userId.first()
            
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Token de usuario no disponible"))
            }
            if (userId == null || userId == 0) {
                return Result.failure(Exception("ID de usuario no disponible o inválido"))
            }
            
            val response = apiService.deleteUser(userId)
            
            if (response.isSuccessful()) {
                sessionManager.clearUserSession()
                authManager.clearAuthData()
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Sesión expirada. Por favor, inicia sesión nuevamente"
                403 -> "No tienes permisos para eliminar este usuario"
                else -> "Error en el servidor: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Cambia la contraseña del usuario
     */
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            val token = authManager.authToken.first()
            val userId = authManager.userId.first()
            
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Token de usuario no disponible"))
            }
            if (userId == null || userId == 0) {
                return Result.failure(Exception("ID de usuario no disponible o inválido"))
            }
            
            val requestBody = mapOf(
                "current_password" to currentPassword,
                "new_password" to newPassword
            )
            
            val response = apiService.changePassword(userId, requestBody)
            
            if (response.isSuccessful()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Contraseña actual incorrecta"
                422 -> "La nueva contraseña no cumple con los requisitos"
                else -> "Error en el servidor: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene estadísticas del usuario
     */
    suspend fun getUserStatistics(): Result<Map<String, Any>> {
        return try {
            val userId = authManager.userId.first()
            
            if (userId == null || userId == 0) {
                return Result.failure(Exception("ID de usuario no disponible"))
            }
            
            val response = apiService.getUserStatistics(userId)
            
            if (response.isSuccessful() && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.getErrorMessage()))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener estadísticas: ${e.message}"))
        }
    }
    
    // Métodos de utilidad para datos estáticos
    fun getNivelesActividad(): List<String> {
        return listOf("sedentario", "ligero", "moderado", "activo", "muy_activo")
    }
    
    fun getObjetivos(): List<String> {
        return listOf("perder_peso", "mantener_peso", "ganar_peso", "ganar_musculo")
    }
    
    fun getGeneros(): List<String> {
        return listOf("M", "F", "O")
    }
    
    /**
     * Prueba la conexión con el servidor
     */
    suspend fun testConnection(): Result<Boolean> {
        return try {
            val response = apiService.testConnection()
            if (response.isSuccessful()) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error de conexión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 
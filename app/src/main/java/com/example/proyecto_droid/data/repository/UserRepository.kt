package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.local.AuthManager
import com.example.proyecto_droid.data.local.SessionManager
import com.example.proyecto_droid.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(context: Context) {
    
    private val sessionManager = SessionManager(context)
    private val authManager = AuthManager(context)
    private val remoteUserRepository = RemoteUserRepository(context)
    
    suspend fun registerUser(user: User): Result<User> {
        return try {
            // Registrar usuario exclusivamente en la API de Laravel
            val apiResult = remoteUserRepository.registerUser(user)
            
            if (apiResult.isSuccess) {
                val registeredUser = apiResult.getOrNull()!!
                
                // Guardar sesión local
                sessionManager.saveUserSession(registeredUser.email, "${registeredUser.nombre} ${registeredUser.apellidos}", registeredUser.id?.toString() ?: "")
                
                Result.success(registeredUser)
            } else {
                Result.failure(Exception(apiResult.exceptionOrNull()?.message ?: "Error en el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Login exclusivamente en la API de Laravel
            val apiResult = remoteUserRepository.loginUser(email, password)
            
            if (apiResult.isSuccess) {
                val (user, token) = apiResult.getOrNull()!!
                if (user.id == null) {
                    return Result.failure(Exception("El usuario no tiene un ID válido"))
                }
                // Guardar datos de autenticación
                authManager.saveAuthData(token, user.id, user.email)
                // Guardar sesión local
                sessionManager.saveUserSession(email, "${user.nombre} ${user.apellidos}", user.id?.toString() ?: "")
                Result.success(user)
            } else {
                Result.failure(Exception(apiResult.exceptionOrNull()?.message ?: "Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logoutUser() {
        sessionManager.clearUserSession()
        authManager.clearAuthData()
    }
    
    fun isUserLoggedIn(): Flow<Boolean> {
        return sessionManager.isLoggedIn
    }
    
    fun getCurrentUserEmail(): Flow<String?> {
        return sessionManager.userEmail
    }
    
    fun getCurrentUserName(): Flow<String?> {
        return sessionManager.userName
    }
    
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
            remoteUserRepository.getUserProfile(userId, token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
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
            val apiResult = remoteUserRepository.updateUserProfile(userId, user, token)
            
            if (apiResult.isSuccess) {
                val updatedUser = apiResult.getOrNull()!!
                sessionManager.updateUserName("${updatedUser.nombre} ${updatedUser.apellidos}")
                Result.success(updatedUser)
            } else {
                Result.failure(Exception(apiResult.exceptionOrNull()?.message ?: "Error al actualizar perfil"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
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
            val apiResult = remoteUserRepository.deleteUser(userId, token)
            
            if (apiResult.isSuccess) {
                sessionManager.clearUserSession()
                authManager.clearAuthData()
                Result.success(Unit)
            } else {
                Result.failure(Exception(apiResult.exceptionOrNull()?.message ?: "Error al eliminar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getNivelesActividad(): List<String> {
        return listOf("sedentario", "ligero", "moderado", "activo", "muy_activo")
    }
    
    fun getObjetivos(): List<String> {
        return listOf("perder_peso", "mantener_peso", "ganar_peso", "ganar_musculo")
    }
    
    fun getGeneros(): List<String> {
        return listOf("M", "F", "O")
    }
} 
package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.model.ApiResponse
import com.example.proyecto_droid.data.model.LoginRequest
import com.example.proyecto_droid.data.model.LoginResponse
import com.example.proyecto_droid.data.model.RegisterRequest
import com.example.proyecto_droid.data.model.RegisterResponse
import com.example.proyecto_droid.data.model.User
import com.example.proyecto_droid.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RemoteUserRepository(private val context: Context) {
    
    private val apiService = RetrofitClient.createApiService(context)
    
    suspend fun loginUser(email: String, password: String): Result<Pair<User, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = apiService.login(loginRequest)
                
                if (response.success && response.data != null) {
                    Result.success(Pair(response.data.usuario, response.data.token))
                } else {
                    Result.failure(Exception(response.message ?: "Error en el login"))
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> Result.failure(Exception("Credenciales incorrectas"))
                    422 -> Result.failure(Exception("Datos de validación incorrectos"))
                    else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
    
    suspend fun registerUser(user: User): Result<Pair<User, String>> {
        return withContext(Dispatchers.IO) {
            try {
                val registerRequest = RegisterRequest(
                    email = user.email,
                    password = user.passwordHash, // Ahora contiene la contraseña en texto plano
                    nombre = user.nombre,
                    apellidos = user.apellidos,
                    fechaNacimiento = user.fechaNacimiento,
                    genero = user.genero,
                    alturaCm = user.alturaCm,
                    pesoKg = user.pesoKg,
                    nivelActividad = user.nivelActividad,
                    objetivoPrincipal = user.objetivoPrincipal
                )
                
                val response = apiService.register(registerRequest)
                
                if (response.success && response.data != null) {
                    Result.success(Pair(response.data.usuario, response.data.token))
                } else {
                    val errorMessage = response.message ?: "Error en el registro"
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    422 -> Result.failure(Exception("Datos de validación incorrectos"))
                    409 -> Result.failure(Exception("El email ya está registrado"))
                    else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
    
    suspend fun getUserProfile(userId: Int, token: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Usar la ruta autenticada correcta
                val response = apiService.getUser(userId, "Bearer $token")
                
                if (response.success && response.data != null) {
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.message ?: "Error al obtener perfil"))
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> Result.failure(Exception("Token no válido"))
                    404 -> Result.failure(Exception("Usuario no encontrado"))
                    else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
    
    suspend fun updateUserProfile(userId: Int, user: User, token: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateUser(userId, user, "Bearer $token")
                
                if (response.success && response.data != null) {
                    Result.success(response.data)
                } else {
                    Result.failure(Exception(response.message ?: "Error al actualizar perfil"))
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> Result.failure(Exception("Token no válido"))
                    404 -> Result.failure(Exception("Usuario no encontrado"))
                    422 -> Result.failure(Exception("Datos de validación incorrectos"))
                    else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
    
    suspend fun deleteUser(userId: Int, token: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteUser(userId, "Bearer $token")
                
                if (response.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(response.message ?: "Error al eliminar usuario"))
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> Result.failure(Exception("Token no válido"))
                    404 -> Result.failure(Exception("Usuario no encontrado"))
                    else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
    
    suspend fun changePassword(userId: Int, currentPassword: String, newPassword: String, token: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = mapOf(
                    "password_actual" to currentPassword,
                    "password_nuevo" to newPassword,
                    "password_nuevo_confirmation" to newPassword
                )
                
                val response = apiService.changePassword(userId, requestBody, "Bearer $token")
                
                if (response.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(response.message ?: "Error al cambiar contraseña"))
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> Result.failure(Exception("Token no válido"))
                    403 -> Result.failure(Exception("Contraseña actual incorrecta"))
                    404 -> Result.failure(Exception("Usuario no encontrado"))
                    422 -> Result.failure(Exception("Datos de validación incorrectos"))
                    else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
} 
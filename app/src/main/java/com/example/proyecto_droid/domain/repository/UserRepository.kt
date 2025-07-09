package com.example.proyecto_droid.domain.repository

import com.example.proyecto_droid.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de usuarios para la capa de dominio
 * Define las operaciones disponibles sin depender de la implementación
 */
interface UserRepository {
    
    /**
     * Registra un nuevo usuario en el sistema
     */
    suspend fun registerUser(user: User): Result<User>
    
    /**
     * Inicia sesión de usuario
     */
    suspend fun loginUser(email: String, password: String): Result<User>
    
    /**
     * Cierra la sesión del usuario
     */
    suspend fun logoutUser()
    
    /**
     * Verifica si el usuario está logueado
     */
    fun isUserLoggedIn(): Flow<Boolean>
    
    /**
     * Obtiene el email del usuario actual
     */
    fun getCurrentUserEmail(): Flow<String?>
    
    /**
     * Obtiene el nombre del usuario actual
     */
    fun getCurrentUserName(): Flow<String?>
    
    /**
     * Obtiene el perfil completo del usuario actual
     */
    suspend fun getCurrentUserProfile(): Result<User>
    
    /**
     * Actualiza el perfil del usuario
     */
    suspend fun updateUser(user: User): Result<User>
    
    /**
     * Cambia la contraseña del usuario
     */
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Boolean>
} 
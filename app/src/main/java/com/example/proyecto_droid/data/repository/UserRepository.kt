package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.local.AppDatabase
import com.example.proyecto_droid.data.local.SessionManager
import com.example.proyecto_droid.data.local.entity.UserEntity
import com.example.proyecto_droid.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

class UserRepository(context: Context) {
    
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val sessionManager = SessionManager(context)
    
    suspend fun registerUser(user: User): Result<User> {
        return try {
            // Verificar si el usuario ya existe
            val existingUser = userDao.userExists(user.email)
            if (existingUser > 0) {
                return Result.failure(Exception("El usuario ya existe"))
            }
            
            // Guardar usuario en la base de datos
            val userEntity = UserEntity.fromUser(user)
            userDao.insertUser(userEntity)
            
            // Guardar sesión
            sessionManager.saveUserSession(user.email, "${user.nombre} ${user.apellidos}", "1")
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val hashedPassword = hashPassword(password)
            
            // Primero intentar con la contraseña hasheada
            var userEntity = userDao.loginUser(email, hashedPassword)
            
            // Si no funciona, intentar con la contraseña sin hashear (para usuarios registrados antes del fix)
            if (userEntity == null) {
                userEntity = userDao.loginUser(email, password)
            }
            
            if (userEntity != null) {
                // Actualizar último login
                userDao.updateLastLogin(email, System.currentTimeMillis())
                
                // Guardar sesión
                sessionManager.saveUserSession(email, "${userEntity.nombre} ${userEntity.apellidos}", "1")
                
                Result.success(userEntity.toUser())
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logoutUser() {
        sessionManager.clearUserSession()
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
    
    suspend fun getLastLoggedInUser(): User? {
        return userDao.getLastLoggedInUser()?.toUser()
    }
    
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toUser()
    }
    
    suspend fun updateUser(user: User) {
        val userEntity = UserEntity.fromUser(user)
        userDao.updateUser(userEntity)
        
        // Actualizar nombre en sesión si es necesario
        sessionManager.updateUserName("${user.nombre} ${user.apellidos}")
    }
    
    suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
        sessionManager.clearUserSession()
    }
    
    suspend fun clearAllUsers() {
        userDao.deleteAllUsers()
        sessionManager.clearUserSession()
    }
    
    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { entity -> entity.toUser() }
        }
    }
    
    fun getNivelesActividad(): List<String> {
        return listOf("Sedentario", "Ligero", "Moderado", "Activo", "Muy activo")
    }
    
    fun getObjetivos(): List<String> {
        return listOf("Perder peso", "Mantener peso", "Ganar peso", "Ganar músculo", "Mejorar salud")
    }
    
    fun getGeneros(): List<String> {
        return listOf("M", "F", "O")
    }
    
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
} 
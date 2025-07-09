package com.example.proyecto_droid.domain.usecase.auth

import com.example.proyecto_droid.domain.model.User
import com.example.proyecto_droid.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para el login de usuarios
 * Encapsula la lógica de negocio para autenticación
 */
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    /**
     * Ejecuta el proceso de login
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result con el usuario logueado o error
     */
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Validación de entrada
        if (email.isBlank()) {
            return Result.failure(Exception("El email es requerido"))
        }
        
        if (password.isBlank()) {
            return Result.failure(Exception("La contraseña es requerida"))
        }
        
        if (!isValidEmail(email)) {
            return Result.failure(Exception("El formato del email no es válido"))
        }
        
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        
        // Ejecutar login a través del repositorio
        return userRepository.loginUser(email, password)
    }
    
    /**
     * Valida el formato del email
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
} 
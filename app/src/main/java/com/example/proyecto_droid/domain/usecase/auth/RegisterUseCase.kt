package com.example.proyecto_droid.domain.usecase.auth

import com.example.proyecto_droid.domain.model.User
import com.example.proyecto_droid.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para el registro de usuarios
 * Encapsula la lógica de negocio para registro
 */
class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    /**
     * Ejecuta el proceso de registro
     * @param user Datos del usuario a registrar
     * @param password Contraseña del usuario
     * @return Result con el usuario registrado o error
     */
    suspend operator fun invoke(user: User, password: String): Result<User> {
        // Validaciones de entrada
        val validationResult = validateUserData(user, password)
        if (validationResult.isFailure) {
            return validationResult
        }
        
        // Crear usuario con contraseña hasheada (simulada)
        val userWithPassword = user.copy()
        
        // Ejecutar registro a través del repositorio
        return userRepository.registerUser(userWithPassword)
    }
    
    /**
     * Valida los datos del usuario
     */
    private fun validateUserData(user: User, password: String): Result<User> {
        when {
            user.email.isBlank() -> return Result.failure(Exception("El email es requerido"))
            user.nombre.isBlank() -> return Result.failure(Exception("El nombre es requerido"))
            user.apellidos.isBlank() -> return Result.failure(Exception("Los apellidos son requeridos"))
            password.isBlank() -> return Result.failure(Exception("La contraseña es requerida"))
            !isValidEmail(user.email) -> return Result.failure(Exception("El formato del email no es válido"))
            password.length < 6 -> return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
            user.fechaNacimiento.isBlank() -> return Result.failure(Exception("La fecha de nacimiento es requerida"))
            user.genero.isBlank() -> return Result.failure(Exception("El género es requerido"))
            user.alturaCm <= 0 -> return Result.failure(Exception("La altura debe ser mayor a 0"))
            user.pesoKg <= 0 -> return Result.failure(Exception("El peso debe ser mayor a 0"))
            else -> return Result.success(user)
        }
    }
    
    /**
     * Valida el formato del email
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
} 
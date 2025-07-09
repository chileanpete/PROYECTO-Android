package com.example.proyecto_droid.domain.usecase.auth

import com.example.proyecto_droid.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para el logout de usuarios
 * Encapsula la lógica de negocio para cerrar sesión
 */
class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    /**
     * Ejecuta el proceso de logout
     */
    suspend operator fun invoke() {
        userRepository.logoutUser()
    }
} 
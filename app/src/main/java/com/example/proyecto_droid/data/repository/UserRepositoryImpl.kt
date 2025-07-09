package com.example.proyecto_droid.data.repository

import android.content.Context
import com.example.proyecto_droid.data.mapper.UserMapper.toDomain
import com.example.proyecto_droid.data.mapper.UserMapper.toData
import com.example.proyecto_droid.domain.model.User as DomainUser
import com.example.proyecto_droid.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementación del repositorio de usuarios
 * Usa el repositorio unificado existente pero adapta los tipos al dominio
 */
class UserRepositoryImpl @Inject constructor(
    private val context: Context
) : UserRepository {
    
    // Usar el repositorio existente
    private val unifiedRepository = UnifiedUserRepository(context)
    
    override suspend fun registerUser(user: DomainUser): Result<DomainUser> {
        val dataUser = user.toData()
        return try {
            val result = unifiedRepository.registerUser(dataUser)
            result.fold(
                onSuccess = { registeredUser ->
                    Result.success(registeredUser.toDomain())
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun loginUser(email: String, password: String): Result<DomainUser> {
        return try {
            val result = unifiedRepository.loginUser(email, password)
            result.fold(
                onSuccess = { loggedUser ->
                    Result.success(loggedUser.toDomain())
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun logoutUser() {
        unifiedRepository.logoutUser()
    }
    
    override fun isUserLoggedIn(): Flow<Boolean> {
        return unifiedRepository.isUserLoggedIn()
    }
    
    override fun getCurrentUserEmail(): Flow<String?> {
        return unifiedRepository.getCurrentUserEmail()
    }
    
    override fun getCurrentUserName(): Flow<String?> {
        return unifiedRepository.getCurrentUserName()
    }
    
    override suspend fun getCurrentUserProfile(): Result<DomainUser> {
        return try {
            val result = unifiedRepository.getCurrentUserProfile()
            result.fold(
                onSuccess = { userProfile ->
                    Result.success(userProfile.toDomain())
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUser(user: DomainUser): Result<DomainUser> {
        val dataUser = user.toData()
        return try {
            val result = unifiedRepository.updateUser(dataUser)
            result.fold(
                onSuccess = { updatedUser ->
                    Result.success(updatedUser.toDomain())
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Boolean> {
        return try {
            val result = unifiedRepository.changePassword(currentPassword, newPassword)
            result.fold(
                onSuccess = { success ->
                    Result.success(success)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 
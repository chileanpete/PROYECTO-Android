package com.example.proyecto_droid.di

import android.content.Context
import com.example.proyecto_droid.data.repository.UserRepositoryImpl
import com.example.proyecto_droid.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para inyección de dependencias de repositorios y servicios de red
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        @ApplicationContext context: Context
    ): UserRepository {
        return UserRepositoryImpl(context)
    }


} 
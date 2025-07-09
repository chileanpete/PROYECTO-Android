package com.example.proyecto_droid.data.remote

object ApiConfig {
    
    // URLs para diferentes entornos
    const val BASE_URL_EMULATOR = "http://192.168.0.6:8000/api/"
    const val BASE_URL_DEVICE = "http://192.168.0.6:8000/api/" // Cambiar por tu IP local
    const val BASE_URL_PRODUCTION = "https://tu-dominio.com/api/" // Cambiar por tu dominio de producción
    
    // Configuración de entorno
    enum class Environment {
        EMULATOR, DEVICE, PRODUCTION
    }
    
    // Cambiar este valor según el entorno de desarrollo
    private val currentEnvironment = Environment.EMULATOR
    
    // Función para obtener la URL base según el entorno
    fun getBaseUrl(): String {
        return when (currentEnvironment) {
            Environment.EMULATOR -> BASE_URL_EMULATOR
            Environment.DEVICE -> BASE_URL_DEVICE
            Environment.PRODUCTION -> BASE_URL_PRODUCTION
        }
    }
    
    // Función para obtener la URL base para dispositivos físicos
    fun getBaseUrlForDevice(): String {
        return BASE_URL_DEVICE
    }
    
    // Función para obtener la URL base para producción
    fun getBaseUrlForProduction(): String {
        return BASE_URL_PRODUCTION
    }
    
    // Función para cambiar el entorno dinámicamente
    fun setEnvironment(environment: Environment) {
        // Esta función permitiría cambiar el entorno en tiempo de ejecución
        // Por ahora, el entorno se configura estáticamente arriba
    }
    
    // Función para verificar si estamos en modo desarrollo
    fun isDevelopmentMode(): Boolean {
        return currentEnvironment != Environment.PRODUCTION
    }
} 
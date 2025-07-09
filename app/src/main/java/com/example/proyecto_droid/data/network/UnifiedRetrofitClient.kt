package com.example.proyecto_droid.data.network

import android.content.Context
import com.example.proyecto_droid.data.remote.ApiConfig
import com.example.proyecto_droid.data.remote.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente unificado de Retrofit para todas las comunicaciones de red.
 * Centraliza la configuración, autenticación y manejo de errores.
 */
object UnifiedRetrofitClient {
    
    private var retrofit: Retrofit? = null
    private var apiService: UnifiedApiService? = null
    
    // Configuración de timeouts - Aumentados para manejar respuestas grandes
    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 120L
    private const val WRITE_TIMEOUT = 60L
    
    /**
     * Configuración personalizada de Gson para el manejo de fechas y null safety
     * Configurado para manejar respuestas grandes y errores de parsing
     */
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .setLenient()
        .serializeNulls()
        .setPrettyPrinting()
        .create()
    
    /**
     * Interceptor de logging para debugging en modo desarrollo
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (ApiConfig.isDevelopmentMode()) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    
    /**
     * Crea el cliente OkHttp con todas las configuraciones necesarias
     */
    private fun createOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(context))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(180L, TimeUnit.SECONDS) // Timeout total para llamadas largas
            .retryOnConnectionFailure(true)
            .build()
    }
    
    /**
     * Crea la instancia de Retrofit con toda la configuración
     */
    private fun createRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.getBaseUrl())
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Obtiene la instancia del servicio API unificado
     * Implementa el patrón Singleton para evitar múltiples instancias
     */
    @Synchronized
    fun getApiService(context: Context): UnifiedApiService {
        if (apiService == null || retrofit == null) {
            retrofit = createRetrofit(context)
            apiService = retrofit!!.create(UnifiedApiService::class.java)
        }
        return apiService!!
    }
    
    /**
     * Reinicia el cliente Retrofit (útil para cambios de configuración)
     */
    fun reset() {
        retrofit = null
        apiService = null
    }
    
    /**
     * Cambia la URL base y reinicia el cliente
     */
    fun changeBaseUrl(context: Context, newBaseUrl: String) {
        reset()
        // Aquí podrías implementar lógica para cambiar la URL dinámicamente
        getApiService(context)
    }
} 
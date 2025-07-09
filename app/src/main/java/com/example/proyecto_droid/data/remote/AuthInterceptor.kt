package com.example.proyecto_droid.data.remote

import android.content.Context
import com.example.proyecto_droid.data.local.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Solo agregar token si no es una petición de login o registro
        val isAuthRequest = originalRequest.url.encodedPath.contains("/usuarios/login") ||
                           originalRequest.url.encodedPath.contains("/usuarios/registro")
        
        if (isAuthRequest) {
            return chain.proceed(originalRequest)
        }
        
        // Obtener el token de autenticación
        val authManager = AuthManager(context)
        val token = runBlocking { authManager.authToken.first() }
        
        return if (token != null) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
} 

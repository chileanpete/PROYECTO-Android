package com.example.proyecto_droid.data.remote

import android.content.Context
import android.util.Log
import com.example.proyecto_droid.data.model.RegisterRequest
import com.example.proyecto_droid.data.model.User
import kotlinx.coroutines.runBlocking

/**
 * Helper para probar la API de registro
 * Este archivo se puede usar para verificar que la conexión con Laravel funcione correctamente
 */
object ApiTestHelper {
    
    private const val TAG = "ApiTestHelper"
    
    /**
     * Prueba el endpoint de registro con datos de ejemplo
     */
    fun testRegistration(context: Context) {
        runBlocking {
            try {
                Log.d(TAG, "Iniciando prueba de registro...")
                
                val apiService = RetrofitClient.createApiService(context)
                
                // Datos de prueba
                val testUser = RegisterRequest(
                    email = "test_${System.currentTimeMillis()}@ejemplo.com",
                    password = "123456",
                    nombre = "Usuario",
                    apellidos = "Prueba",
                    fechaNacimiento = "1990-01-01",
                    genero = "M",
                    alturaCm = 175,
                    pesoKg = 70.0,
                    nivelActividad = "moderado",
                    objetivoPrincipal = "mantener_peso"
                )
                
                Log.d(TAG, "Enviando datos de prueba: ${testUser.email}")
                
                val response = apiService.register(testUser)
                
                if (response.success) {
                    Log.d(TAG, "✅ Registro exitoso!")
                    Log.d(TAG, "Usuario creado: ${response.data?.usuario?.nombre} ${response.data?.usuario?.apellidos}")
                    Log.d(TAG, "ID: ${response.data?.usuario?.id}")
                    Log.d(TAG, "Token: ${response.data?.token}")
                } else {
                    Log.e(TAG, "❌ Error en registro: ${response.message}")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error de conexión: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Prueba el endpoint de login
     */
    fun testLogin(context: Context, email: String, password: String) {
        runBlocking {
            try {
                Log.d(TAG, "Iniciando prueba de login...")
                
                val apiService = RetrofitClient.createApiService(context)
                val loginRequest = com.example.proyecto_droid.data.model.LoginRequest(email, password)
                
                val response = apiService.login(loginRequest)
                
                if (response.success) {
                    Log.d(TAG, "✅ Login exitoso!")
                    Log.d(TAG, "Token: ${response.data?.token}")
                    Log.d(TAG, "Usuario: ${response.data?.usuario?.nombre}")
                } else {
                    Log.e(TAG, "❌ Error en login: ${response.message}")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error de conexión: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Verifica la conectividad con el servidor
     */
    fun testConnectivity(context: Context) {
        runBlocking {
            try {
                Log.d(TAG, "Verificando conectividad...")
                Log.d(TAG, "URL base: ${ApiConfig.getBaseUrl()}")
                
                val apiService = RetrofitClient.createApiService(context)
                
                // Intentar hacer una petición simple
                val response = apiService.login(
                    com.example.proyecto_droid.data.model.LoginRequest("test@test.com", "test")
                )
                
                // Si llegamos aquí, la conexión funciona
                Log.d(TAG, "✅ Conectividad OK - Servidor responde")
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error de conectividad: ${e.message}")
                Log.e(TAG, "Verifica que:")
                Log.e(TAG, "1. Laravel esté corriendo (php artisan serve)")
                Log.e(TAG, "2. La URL en ApiConfig sea correcta")
                Log.e(TAG, "3. El dispositivo tenga internet")
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Ejecuta todas las pruebas
     */
    fun runAllTests(context: Context) {
        Log.d(TAG, "🚀 Iniciando todas las pruebas de API...")
        
        // 1. Probar conectividad
        testConnectivity(context)
        
        // 2. Probar registro
        testRegistration(context)
        
        // 3. Probar login con el usuario creado
        val testEmail = "test_${System.currentTimeMillis()}@ejemplo.com"
        testLogin(context, testEmail, "123456")
        
        Log.d(TAG, "✅ Todas las pruebas completadas")
    }
} 
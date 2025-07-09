package com.example.proyecto_droid.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object NetworkHelper {
    
    private const val TAG = "NetworkHelper"
    
    /**
     * Verifica si hay conexión a internet
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    
    /**
     * Verifica si el servidor Laravel está respondiendo
     */
    suspend fun isLaravelServerReachable(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("${ApiConfig.getBaseUrl()}usuarios/login")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "OPTIONS"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                
                val responseCode = connection.responseCode
                Log.d(TAG, "Servidor Laravel responde con código: $responseCode")
                
                // Cualquier respuesta significa que el servidor está activo
                responseCode in 200..599
            } catch (e: Exception) {
                Log.e(TAG, "Error al verificar servidor Laravel: ${e.message}")
                false
            }
        }
    }
    
    /**
     * Diagnostica problemas de conectividad
     */
    suspend fun diagnoseConnection(context: Context): String {
        val diagnostics = mutableListOf<String>()
        
        // Verificar conexión a internet
        if (isNetworkAvailable(context)) {
            diagnostics.add("✅ Conexión a internet: OK")
        } else {
            diagnostics.add("❌ Conexión a internet: FALLO")
            return diagnostics.joinToString("\n")
        }
        
        // Verificar servidor Laravel
        if (isLaravelServerReachable()) {
            diagnostics.add("✅ Servidor Laravel: RESPONDE")
        } else {
            diagnostics.add("❌ Servidor Laravel: NO RESPONDE")
            diagnostics.add("   - Verifica que Laravel esté corriendo en puerto 8000")
            diagnostics.add("   - Verifica la URL: ${ApiConfig.getBaseUrl()}")
        }
        
        // Verificar configuración de red
        diagnostics.add("📋 Configuración actual:")
        diagnostics.add("   - URL Base: ${ApiConfig.getBaseUrl()}")
        diagnostics.add("   - Entorno: ${ApiConfig.Environment.EMULATOR}")
        diagnostics.add("   - Modo desarrollo: ${ApiConfig.isDevelopmentMode()}")
        
        return diagnostics.joinToString("\n")
    }
    
    /**
     * Obtiene información de red para debugging
     */
    fun getNetworkInfo(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(network)
        
        return buildString {
            appendLine("=== Información de Red ===")
            appendLine("Red activa: ${network != null}")
            if (activeNetwork != null) {
                appendLine("WiFi: ${activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}")
                appendLine("Cellular: ${activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}")
                appendLine("Ethernet: ${activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)}")
            }
            appendLine("URL Base: ${ApiConfig.getBaseUrl()}")
            appendLine("==========================")
        }
    }
} 
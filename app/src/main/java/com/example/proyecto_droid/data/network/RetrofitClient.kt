package com.example.proyecto_droid.data.network

import com.example.proyecto_droid.data.network.services.RegistroConsumoService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "http://192.168.0.6:8000/"

    // Token fijo de pruebas (cámbialo por el que obtengas en login real)
    private fun getAuthToken(): String? {
        return "Bearer 3|0ZWda5EBG6PJKs9OdMQg2i4VR2WBtTGUgMtZMU2Lfa8ec54c"
    }

    private val authInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val token = getAuthToken()
        val requestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", token)
        }
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    val registroConsumoService: RegistroConsumoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RegistroConsumoService::class.java)
    }
} 
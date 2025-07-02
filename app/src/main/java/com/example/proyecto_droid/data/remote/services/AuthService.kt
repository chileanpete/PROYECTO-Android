package com.example.proyecto_droid.data.remote.services

import com.example.proyecto_droid.model.LoginRequest
import com.example.proyecto_droid.model.LoginResponse
import com.example.proyecto_droid.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/profile")
    suspend fun getUser(): User

    @POST("logout")
    suspend fun logout()
}
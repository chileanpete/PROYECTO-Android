package com.example.proyecto_droid.data.remote

import com.example.proyecto_droid.data.model.ApiResponse
import com.example.proyecto_droid.data.model.LoginRequest
import com.example.proyecto_droid.data.model.LoginResponse
import com.example.proyecto_droid.data.model.RegisterRequest
import com.example.proyecto_droid.data.model.User
import retrofit2.http.*

interface ApiService {
    
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>
    
    @POST("usuarios/registro")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<User>
    
    @GET("usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int, @Header("Authorization") token: String): ApiResponse<User>
    
    @PUT("usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: Int, 
        @Body user: User, 
        @Header("Authorization") token: String
    ): ApiResponse<User>
    
    @DELETE("usuarios/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int, 
        @Header("Authorization") token: String
    ): ApiResponse<Unit>
} 
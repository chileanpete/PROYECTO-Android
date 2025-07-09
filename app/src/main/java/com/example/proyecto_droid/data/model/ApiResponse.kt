package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: T?,
    
    @SerializedName("errors")
    val errors: Map<String, List<String>>? = null
)

data class LoginResponse(
    @SerializedName("usuario")
    val usuario: User,
    
    @SerializedName("token")
    val token: String
)

data class RegisterResponse(
    @SerializedName("usuario")
    val usuario: User,
    
    @SerializedName("token")
    val token: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("apellidos")
    val apellidos: String,
    
    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String,
    
    @SerializedName("genero")
    val genero: String,
    
    @SerializedName("altura_cm")
    val alturaCm: Int,
    
    @SerializedName("peso_kg")
    val pesoKg: Double,
    
    @SerializedName("nivel_actividad")
    val nivelActividad: String,
    
    @SerializedName("objetivo_principal")
    val objetivoPrincipal: String
) 
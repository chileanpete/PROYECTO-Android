package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para request de login
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

/**
 * Modelo para request de registro
 */
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
    val objetivoPrincipal: String,
    
    @SerializedName("preferencias_alimentarias")
    val preferenciasAlimentarias: String? = null,
    
    @SerializedName("alergias")
    val alergias: String? = null
)

/**
 * Modelo para respuesta de login
 */
data class LoginResponse(
    @SerializedName("usuario")
    val usuario: User,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("expires_at")
    val expiresAt: String? = null
)

/**
 * Modelo para respuesta de registro
 */
data class RegisterResponse(
    @SerializedName("usuario")
    val usuario: User,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("message")
    val message: String? = null
) 
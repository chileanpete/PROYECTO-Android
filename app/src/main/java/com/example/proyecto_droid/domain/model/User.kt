package com.example.proyecto_droid.domain.model

/**
 * Modelo de dominio para Usuario
 * Representa la entidad central de usuario en la lógica de negocio
 */
data class User(
    val id: Int? = null,
    val email: String = "",
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val alturaCm: Int = 0,
    val pesoKg: Double = 0.0,
    val nivelActividad: String = "",
    val objetivoPrincipal: String = "",
    val preferenciasAlimentarias: String = "",
    val alergias: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
) 
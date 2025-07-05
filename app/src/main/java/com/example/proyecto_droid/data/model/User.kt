package com.example.proyecto_droid.data.model

import java.util.Date

data class User(
    val id: Int? = null,
    val email: String = "",
    val passwordHash: String = "",
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
    val fechaRegistro: Date? = null,
    val fechaUltimoAcceso: Date? = null,
    val activo: Boolean = true,
    val puntosTotales: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "email" to email,
            "password_hash" to passwordHash,
            "nombre" to nombre,
            "apellidos" to apellidos,
            "fecha_nacimiento" to fechaNacimiento,
            "genero" to genero,
            "altura_cm" to alturaCm,
            "peso_kg" to pesoKg,
            "nivel_actividad" to nivelActividad,
            "objetivo_principal" to objetivoPrincipal,
            "preferencias_alimentarias" to preferenciasAlimentarias,
            "alergias" to alergias
        )
    }
    
    fun isValid(): Boolean {
        return email.isNotBlank() &&
               passwordHash.isNotBlank() &&
               nombre.isNotBlank() &&
               apellidos.isNotBlank() &&
               fechaNacimiento.isNotBlank() &&
               genero.isNotBlank() &&
               alturaCm > 0 &&
               pesoKg > 0 &&
               nivelActividad.isNotBlank() &&
               objetivoPrincipal.isNotBlank()
    }
    
    fun getFullName(): String = "$nombre $apellidos".trim()
    
    fun getDisplayGenero(): String = when(genero) {
        "M" -> "Masculino"
        "F" -> "Femenino"
        "O" -> "Otro"
        else -> ""
    }
} 
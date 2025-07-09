package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @SerializedName("id_usuario")
    val id: Int? = null,
    
    @SerializedName("email")
    val email: String = "",
    
    @SerializedName("password_hash")
    val passwordHash: String = "",
    
    @SerializedName("nombre")
    val nombre: String = "",
    
    @SerializedName("apellidos")
    val apellidos: String = "",
    
    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String = "",
    
    @SerializedName("genero")
    val genero: String = "",
    
    @SerializedName("altura_cm")
    val alturaCm: Int = 0,
    
    @SerializedName("peso_kg")
    val pesoKg: Double = 0.0,
    
    @SerializedName("nivel_actividad")
    val nivelActividad: String = "",
    
    @SerializedName("objetivo_principal")
    val objetivoPrincipal: String = "",
    
    @SerializedName("preferencias_alimentarias")
    val preferenciasAlimentarias: String? = null,
    
    @SerializedName("alergias")
    val alergias: String? = null,
    
    @SerializedName("fecha_registro")
    val fechaRegistro: String? = null,
    
    @SerializedName("fecha_ultimo_acceso")
    val fechaUltimoAcceso: String? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("puntos_totales")
    val puntosTotales: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "email" to email,
            "password" to passwordHash, // Para registro, enviamos password sin hash
            "nombre" to nombre,
            "apellidos" to apellidos,
            "fecha_nacimiento" to fechaNacimiento,
            "genero" to genero,
            "altura_cm" to alturaCm,
            "peso_kg" to pesoKg,
            "nivel_actividad" to nivelActividad,
            "objetivo_principal" to objetivoPrincipal,
            "preferencias_alimentarias" to (preferenciasAlimentarias ?: ""),
            "alergias" to (alergias ?: "")
        )
    }
    
    fun toRegisterMap(): Map<String, Any> {
        return mapOf(
            "email" to email,
            "password" to passwordHash, // Para registro, enviamos password sin hash
            "nombre" to nombre,
            "apellidos" to apellidos,
            "fecha_nacimiento" to fechaNacimiento,
            "genero" to genero,
            "altura_cm" to alturaCm,
            "peso_kg" to pesoKg,
            "nivel_actividad" to nivelActividad,
            "objetivo_principal" to objetivoPrincipal,
            "preferencias_alimentarias" to (preferenciasAlimentarias ?: ""),
            "alergias" to (alergias ?: "")
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
    
    fun getDisplayNivelActividad(): String = when(nivelActividad) {
        "sedentario" -> "Sedentario"
        "ligero" -> "Ligero"
        "moderado" -> "Moderado"
        "activo" -> "Activo"
        "muy_activo" -> "Muy Activo"
        else -> nivelActividad
    }
    
    fun getDisplayObjetivo(): String = when(objetivoPrincipal) {
        "perder_peso" -> "Perder Peso"
        "mantener_peso" -> "Mantener Peso"
        "ganar_peso" -> "Ganar Peso"
        "ganar_musculo" -> "Ganar Músculo"
        else -> objetivoPrincipal
    }
} 
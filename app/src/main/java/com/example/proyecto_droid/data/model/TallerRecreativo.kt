package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para Taller Recreativo
 */
data class TallerRecreativo(
    @SerializedName("id_taller")
    val idTaller: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String,
    
    @SerializedName("instructor")
    val instructor: String = "",
    
    @SerializedName("categoria")
    val categoria: String = "",
    
    @SerializedName("duracion_minutos")
    val duracionMinutos: Int = 0,
    
    @SerializedName("nivel_dificultad")
    val nivelDificultad: Int = 1,
    
    @SerializedName("cupo_maximo")
    val cupoMaximo: Int = 0,
    
    @SerializedName("costo")
    val costo: Double = 0.0,
    
    @SerializedName("ubicacion")
    val ubicacion: String = "",
    
    @SerializedName("fecha_inicio")
    val fechaInicio: String = "",
    
    @SerializedName("fecha_fin")
    val fechaFin: String = "",
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("imagen_url")
    val imagenUrl: String = "",
    
    @SerializedName("requisitos")
    val requisitos: String = ""
) 
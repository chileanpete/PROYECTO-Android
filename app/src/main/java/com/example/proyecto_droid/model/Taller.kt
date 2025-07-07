package com.example.proyecto_droid.model

import com.google.gson.annotations.SerializedName

data class TallerRecreativo(
    @SerializedName("id_taller")
    val idTaller: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String,
    
    @SerializedName("instructor")
    val instructor: String,
    
    @SerializedName("categoria")
    val categoria: String,
    
    @SerializedName("duracion_minutos")
    val duracionMinutos: Int,
    
    @SerializedName("nivel_dificultad")
    val nivelDificultad: Int,
    
    @SerializedName("cupo_maximo")
    val cupoMaximo: Int,
    
    @SerializedName("costo")
    val costo: Double,
    
    @SerializedName("ubicacion")
    val ubicacion: String,
    
    @SerializedName("fecha_inicio")
    val fechaInicio: String,
    
    @SerializedName("fecha_fin")
    val fechaFin: String,
    
    @SerializedName("activo")
    val activo: Boolean,
    
    @SerializedName("imagen_url")
    val imagenUrl: String?,
    
    @SerializedName("requisitos")
    val requisitos: String?
)

data class InscripcionTaller(
    @SerializedName("id_inscripcion")
    val idInscripcion: Int,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("id_taller")
    val idTaller: Int,
    
    @SerializedName("fecha_inscripcion")
    val fechaInscripcion: String,
    
    @SerializedName("estado")
    val estado: String,
    
    @SerializedName("puntos_obtenidos")
    val puntosObtenidos: Int,
    
    @SerializedName("calificacion")
    val calificacion: Int?,
    
    @SerializedName("comentario")
    val comentario: String?,
    
    @SerializedName("taller")
    val taller: TallerRecreativo? = null
) 
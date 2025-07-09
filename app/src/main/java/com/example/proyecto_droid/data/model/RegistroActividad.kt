package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para Registro de Actividad Física
 */
data class RegistroActividad(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("fecha_actividad")
    val fechaActividad: String,
    
    @SerializedName("hora_inicio")
    val horaInicio: String? = null,
    
    @SerializedName("hora_fin")
    val horaFin: String? = null,
    
    @SerializedName("duracion_minutos")
    val duracionMinutos: Int,
    
    @SerializedName("calorias_quemadas")
    val caloriasQuemadas: Int? = null,
    
    @SerializedName("intensidad")
    val intensidad: Int? = null, // 1-5 escala
    
    @SerializedName("comentario")
    val comentario: String? = null,
    
    @SerializedName("completada")
    val completada: Boolean = false,
    
    @SerializedName("puntos_obtenidos")
    val puntosObtenidos: Int? = null,
    
    @SerializedName("id_tipo_ejercicio")
    val idTipoEjercicio: Int? = null,
    
    @SerializedName("id_rutina_ejercicio")
    val idRutinaEjercicio: Int? = null,
    
    @SerializedName("tipo_ejercicio")
    val tipoEjercicio: TipoEjercicio? = null,
    
    @SerializedName("rutina_ejercicio")
    val rutinaEjercicio: Any? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
) 
package com.example.proyecto_droid.model

import com.google.gson.annotations.SerializedName

data class RegistroActividad(
    @SerializedName("id_actividad")
    val idActividad: Int,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("id_rutina")
    val idRutina: Int?,
    
    @SerializedName("id_tipo_ejercicio")
    val idTipoEjercicio: Int?,
    
    @SerializedName("fecha_actividad")
    val fechaActividad: String,
    
    @SerializedName("hora_inicio")
    val horaInicio: String,
    
    @SerializedName("hora_fin")
    val horaFin: String,
    
    @SerializedName("duracion_minutos")
    val duracionMinutos: Int,
    
    @SerializedName("calorias_quemadas")
    val caloriasQuemadas: Int,
    
    @SerializedName("intensidad")
    val intensidad: Int,
    
    @SerializedName("comentario")
    val comentario: String?,
    
    @SerializedName("puntos_obtenidos")
    val puntosObtenidos: Int,
    
    @SerializedName("completada")
    val completada: Boolean,
    
    @SerializedName("tipo_ejercicio")
    val tipoEjercicio: TipoEjercicio? = null,
    
    @SerializedName("rutina_ejercicio")
    val rutinaEjercicio: RutinaEjercicio? = null
)

data class TipoEjercicio(
    @SerializedName("id_tipo_ejercicio")
    val idTipoEjercicio: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String?,
    
    @SerializedName("categoria")
    val categoria: String,
    
    @SerializedName("intensidad_recomendada")
    val intensidadRecomendada: Int
)

data class RutinaEjercicio(
    @SerializedName("id_rutina")
    val idRutina: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String?,
    
    @SerializedName("duracion_minutos")
    val duracionMinutos: Int,
    
    @SerializedName("nivel_dificultad")
    val nivelDificultad: Int,
    
    @SerializedName("calorias_estimadas")
    val caloriasEstimadas: Int
) 
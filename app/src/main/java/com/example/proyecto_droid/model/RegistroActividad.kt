package com.example.proyecto_droid.model

// Modelo compatible con la respuesta del backend
// Puedes agregar/quitar campos según la respuesta real

data class RegistroActividad(
    val id: Int? = null,
    val id_usuario: Int? = null,
    val fecha_actividad: String? = null,
    val hora_inicio: String? = null,
    val hora_fin: String? = null,
    val duracion_minutos: Int? = null,
    val calorias_quemadas: Int? = null,
    val intensidad: Int? = null,
    val comentario: String? = null,
    val completada: Boolean? = null,
    val puntos_obtenidos: Int? = null,
    val tipoEjercicio: TipoEjercicio? = null
)

// Puedes definir TipoEjercicio como un data class aparte si lo necesitas
// data class TipoEjercicio(val id_tipo_ejercicio: Int, val nombre: String, ...) 
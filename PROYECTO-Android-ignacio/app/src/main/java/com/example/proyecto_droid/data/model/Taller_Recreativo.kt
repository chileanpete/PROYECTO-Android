package com.example.proyecto_droid.data.model

import java.util.Date

data class Taller_Recreativo(
    val id_taller: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val instructor: String = "",
    val categoria: String = "",
    var duracion_minutos: Int = 0,
    var nivel_dificultad: Int = 0,
    var cupo_maximo: Int = 0,
    var costo: Float,
    val ubicacion: String = "",
    val fecha_inicio: Date? = null,
    val fecha_fin: Date? = null,
    val activo: Boolean = true,
    val imagen_url:  String = "",
    val requisitos: String = ""
)
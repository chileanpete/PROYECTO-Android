package com.example.proyecto_droid.model

data class RegistroActividad(
    val idActividad: Int,
    val fecha: String,
    val tipoEjercicio: String,
    val duracionMin: Int,
    val intensidad: Int,
    val comentario: String?
) 
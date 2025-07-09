package com.example.proyecto_droid.model

data class TipoEjercicio(
    val idTipoEjercicio: Int,
    val nombre: String,
    val descripcion: String?,
    val categoria: String?,
    val intensidadRecomendada: Int?
) 
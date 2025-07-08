package com.example.proyecto_droid.model

data class RegistroConsumo(
    val idConsumo: Int,
    val fecha: String,
    val plato: String,
    val cantidad: Int,
    val comentario: String?
) 
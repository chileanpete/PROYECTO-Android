package com.example.proyecto_droid.data.model

data class DesafioSugeridoResponse(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo_desafio: String,
    val objetivos_relacionados: List<String>
) 
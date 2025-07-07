package com.example.proyecto_droid.model

enum class TipoDesafio {
    DIARIO, SEMANAL
}

data class Desafio(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: TipoDesafio,
    val objetivosRelacionados: List<String>
)

data class DesafioApiModel(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo_desafio: String,
    val objetivos_relacionados: List<String>
)

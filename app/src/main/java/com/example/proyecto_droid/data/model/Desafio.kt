package com.example.proyecto_droid.data.model

enum class TipoDesafio { DIARIO, SEMANAL }

data class Desafio(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val tipo: TipoDesafio,
    val objetivosRelacionados: List<String>
) 
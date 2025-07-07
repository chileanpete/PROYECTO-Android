package com.example.proyecto_droid.model

data class DesafioSugeridoResponse(
    val usuario_id: Int,
    val imc: Double,
    val objetivo: String,
    val desafios_sugeridos: List<DesafioSugeridoDto>
)

data class DesafioSugeridoDto(
    val id_desafio: Int,
    val titulo: String,
    val descripcion: String,
    val tipo_desafio: String,
    val categoria: String,
    val objetivo_valor: Int,
    val unidad_medida: String,
    val puntos_recompensa: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val activo: Boolean,
    val dificultad: Int,
    val icono: String?
)

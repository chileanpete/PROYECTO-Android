package com.example.proyecto_droid.model

data class Plato(
    val idPlato: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double?,
    val caloriasPorPorcion: Int?,
    val proteinasG: Double?,
    val carbohidratosG: Double?,
    val grasasG: Double?,
    val esVegetariano: Boolean?,
    val esVegano: Boolean?,
    val sinGluten: Boolean?,
    val imagenUrl: String?,
    val lugar: String?,
    val categoria: String?
) 
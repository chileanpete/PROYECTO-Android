package com.example.proyecto_droid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Plato(
    val id: Int,
    val idLugar:Int,
    val idCategoria:Int,
    val nombrePlato:String,
    val descripcionPlato: String,
    val precioPlato: Double,
    val caloriasPlato: Int,
    val protePlato:Double,
    val carbPlato: Double,
    val grasaPlato: Double,
    val fibraPlato: Double,
    val azucarPlato: Double,
    val sodioPlato: Double,
    val disponibilidadPlato: Boolean,
    val esVegetariano: Boolean,
    val esVegano: Boolean,
    val sinGluten: Boolean,
    val imagenUrl: String
)
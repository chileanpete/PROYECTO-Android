package com.example.proyecto_droid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Plato(
    val idPlato: Int,
    val nombrePlato:String,
    val descripcionPlato: String,

    val caloriasPlato: Int,
    val carbPlato: Int,
    val protePlato:Int,
    val grasaPlato: Int,
    val idLocal:Int
)
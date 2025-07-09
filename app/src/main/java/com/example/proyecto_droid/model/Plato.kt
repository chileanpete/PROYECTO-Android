package com.example.proyecto_droid.model

import com.google.gson.annotations.SerializedName

data class Plato(
    @SerializedName("id_plato")
    val idPlato: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("precio")
    val precio: String?,
    @SerializedName("calorias_por_porcion")
    val caloriasPorPorcion: String?,
    @SerializedName("proteinas_g")
    val proteinasG: String?,
    @SerializedName("carbohidratos_g")
    val carbohidratosG: String?,
    @SerializedName("grasas_g")
    val grasasG: String?,
    @SerializedName("es_vegetariano")
    val esVegetariano: Boolean?,
    @SerializedName("es_vegano")
    val esVegano: Boolean?,
    @SerializedName("sin_gluten")
    val sinGluten: Boolean?,
    @SerializedName("imagen_url")
    val imagenUrl: String?,
    @SerializedName("lugar")
    val lugar: Any?,
    @SerializedName("categoria")
    val categoria: Any?
) 
package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para Plato de comida
 */
data class Plato(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("id_plato")
    val idPlato: Int? = null,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String? = null,
    
    @SerializedName("precio")
    val precio: String? = null,
    
    @SerializedName("calorias_por_porcion")
    val caloriasPorPorcion: String? = null,
    
    @SerializedName("proteinas_g")
    val proteinasG: String? = null,
    
    @SerializedName("carbohidratos_g")
    val carbohidratosG: String? = null,
    
    @SerializedName("grasas_g")
    val grasasG: String? = null,
    
    @SerializedName("es_vegetariano")
    val esVegetariano: Boolean? = null,
    
    @SerializedName("es_vegano")
    val esVegano: Boolean? = null,
    
    @SerializedName("sin_gluten")
    val sinGluten: Boolean? = null,
    
    @SerializedName("imagen_url")
    val imagenUrl: String? = null,
    
    @SerializedName("id_lugar")
    val idLugar: Int? = null,
    
    @SerializedName("id_categoria")
    val idCategoria: Int? = null,
    
    @SerializedName("lugar")
    val lugar: LugarComida? = null,
    
    @SerializedName("categoria")
    val categoria: CategoriaComida? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true
) 
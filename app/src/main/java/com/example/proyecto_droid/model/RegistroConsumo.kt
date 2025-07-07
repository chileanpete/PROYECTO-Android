package com.example.proyecto_droid.model

import com.google.gson.annotations.SerializedName

data class RegistroConsumo(
    @SerializedName("id_consumo")
    val idConsumo: Int,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("fecha_consumo")
    val fechaConsumo: String,
    
    @SerializedName("hora_consumo")
    val horaConsumo: String,
    
    @SerializedName("porciones")
    val porciones: Double,
    
    @SerializedName("calorias_totales")
    val caloriasTotales: Int,
    
    @SerializedName("valoracion")
    val valoracion: Int?,
    
    @SerializedName("comentario")
    val comentario: String?,
    
    @SerializedName("puntos_obtenidos")
    val puntosObtenidos: Int,
    
    @SerializedName("plato")
    val plato: Plato? = null
)

data class Plato(
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String?,
    
    @SerializedName("precio")
    val precio: Double,
    
    @SerializedName("calorias_por_porcion")
    val caloriasPorPorcion: Int,
    
    @SerializedName("proteinas_g")
    val proteinasG: Double,
    
    @SerializedName("carbohidratos_g")
    val carbohidratosG: Double,
    
    @SerializedName("grasas_g")
    val grasasG: Double,
    
    @SerializedName("es_vegetariano")
    val esVegetariano: Boolean,
    
    @SerializedName("es_vegano")
    val esVegano: Boolean,
    
    @SerializedName("sin_gluten")
    val sinGluten: Boolean,
    
    @SerializedName("imagen_url")
    val imagenUrl: String?,
    
    @SerializedName("lugar")
    val lugar: LugarComida? = null,
    
    @SerializedName("categoria")
    val categoria: CategoriaComida? = null
)

data class LugarComida(
    @SerializedName("id_lugar")
    val idLugar: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("direccion")
    val direccion: String,
    
    @SerializedName("tipo")
    val tipo: String
)

data class CategoriaComida(
    @SerializedName("id_categoria")
    val idCategoria: Int,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String?
) 
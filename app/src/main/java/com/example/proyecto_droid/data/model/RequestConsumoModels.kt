package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para crear un nuevo registro de consumo
 */
data class CrearRegistroConsumoRequest(
    @SerializedName("id_plato")
    val id_plato: Int,
    
    @SerializedName("fecha_consumo")
    val fecha_consumo: String,
    
    @SerializedName("hora_consumo")
    val hora_consumo: String,
    
    @SerializedName("porciones")
    val porciones: Double,
    
    @SerializedName("valoracion")
    val valoracion: Int? = null,
    
    @SerializedName("comentario")
    val comentario: String? = null,
    
    @SerializedName("calorias_totales")
    val calorias_totales: Int? = null
) 
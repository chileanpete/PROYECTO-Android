package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para Registro de Consumo de Alimentos
 */
data class RegistroConsumo(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("id_consumo")
    val idConsumo: Int? = null,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("fecha_consumo")
    val fechaConsumo: String,
    
    @SerializedName("hora_consumo")
    val horaConsumo: String? = null,
    
    @SerializedName("porciones")
    val porciones: Double = 1.0,
    
    @SerializedName("cantidad")
    val cantidad: Double = 1.0,
    
    @SerializedName("unidad")
    val unidad: String = "porcion",
    
    @SerializedName("calorias_totales")
    val caloriasTotales: Int? = null,
    
    @SerializedName("valoracion")
    val valoracion: Int? = null, // 1-5 escala
    
    @SerializedName("comentario")
    val comentario: String? = null,
    
    @SerializedName("tipo_comida")
    val tipoComida: String? = null, // desayuno, almuerzo, cena, snack
    
    @SerializedName("plato")
    val plato: Plato? = null,
    
    @SerializedName("usuario")
    val usuario: Any? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
) 
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
    val porciones: String,
    @SerializedName("calorias_totales")
    val caloriasTotales: Int,
    @SerializedName("valoracion")
    val valoracion: Int?,
    @SerializedName("comentario")
    val comentario: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("usuario")
    val usuario: Any?,
    @SerializedName("plato")
    val plato: Any?
) 
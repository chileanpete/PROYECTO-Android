package com.example.proyecto_droid.data.model

import androidx.datastore.preferences.protobuf.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Categoria(
    val id_categoria: Int,
    val nombre: String,
    val imagen_url: String? = null
)
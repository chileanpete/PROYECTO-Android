package com.example.proyecto_droid.data.model

import androidx.datastore.preferences.protobuf.Timestamp
import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id_categoria: Int = 0,
    val nombre: String,
    val descripcion: String? = null,  // Opcional
    val imagen_url: String? = null
)
//@Serializable
//data class Categoria(
//    val id_categoria: Int,
//    val nombre: String,
//    val imagen_url: String? = null
//)
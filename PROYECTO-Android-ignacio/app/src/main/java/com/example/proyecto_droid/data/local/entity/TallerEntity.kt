package com.example.proyecto_droid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "talleres")
data class TallerEntity(
    @PrimaryKey(autoGenerate = true) val id_taller: Int = 0,
    val nombre: String,
    val descripcion: String,
    val instructor: String = "",
    val categoria: String = "",
    var duracion_minutos: Int = 0,
    var nivel_dificultad: String = "",
    var cupo_maximo: Int = 0,
    var costo: Double,
    val ubicacion: String = "",
    val fecha_inicio: String = "",
    val fecha_fin: String = "",
    val activo: Boolean = true,
    val imagen_url: String = "",
    val requisitos: String = ""
)

@Entity(tableName = "platos_favoritos")
data class PlatoFavoritoEntity(
    @PrimaryKey(autoGenerate = true) val id_favorito: Int = 0,
    var id_usuario: Int = 0,
    var id_plato: Int = 0,
    val fecha_agregado: String = ""
)
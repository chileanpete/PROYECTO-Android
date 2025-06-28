package com.example.proyecto_droid.data.model

import androidx.datastore.preferences.protobuf.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Lugar(
    val id: Int,
    val nombre:String,
    val tipo:String,
    val ubicacion:String,
    val coordenadas_lat:Double,
    val coordenadas_long:Double,
    val hora_apertura:Timestamp,
    val hora_cierre:Timestamp,
    val telefono:String,
    val calificacion_promedio:Double,
    val precio_medio:Double,
    val activo:Boolean,
    val imagen_url:String
)
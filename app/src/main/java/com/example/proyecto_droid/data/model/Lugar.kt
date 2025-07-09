package com.example.proyecto_droid.data.model

import androidx.datastore.preferences.protobuf.Timestamp
import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lugares")
data class Lugar(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // AutoGenerate requiere valor por defecto
    val nombre: String,
    val tipo: String,
    val ubicacion: String? = null,  // Opcional
    val coordenadas_lat: Double? = null,
    val coordenadas_long: Double? = null,
    val hora_apertura: Timestamp? = null,
    val hora_cierre: Timestamp? = null,
    val telefono: String? = null,
    val calificacion_promedio: Double? = null,
    val precio_medio: Double? = null,
    val activo: Boolean = true,  // Valor por defecto
    val imagen_url: String? = null
)

//@Serializable
//data class Lugar(
//    val id: Int,
//    val nombre:String,
//    val tipo:String,
//    val ubicacion:String,
//    val coordenadas_lat:Double,
//    val coordenadas_long:Double,
//    val hora_apertura:Timestamp,
//    val hora_cierre:Timestamp,
//    val telefono:String,
//    val calificacion_promedio:Double,
//    val precio_medio:Double,
//    val activo:Boolean,
//    val imagen_url:String
//)
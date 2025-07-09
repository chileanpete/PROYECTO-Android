package com.example.proyecto_droid.data.model

import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "platos",
    foreignKeys = [
        ForeignKey(
            entity = Lugar::class,
            parentColumns = ["id"],
            childColumns = ["idLugar"],
            onDelete = ForeignKey.CASCADE,  // Si se elimina el lugar, se eliminan sus platos
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id_categoria"],
            childColumns = ["idCategoria"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idLugar"), Index("idCategoria")]  // Mejora rendimiento en búsquedas
)
data class Plato(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idLugar: Int? = null,  // Opcional (puede no estar asociado a un lugar)
    val idCategoria: Int,      // Requerido
    val nombrePlato: String,
    val descripcionPlato: String? = null,
    val precioPlato: Double,
    val caloriasPlato: Int? = null,
    val protePlato: Double? = null,
    val carbPlato: Double? = null,
    val grasaPlato: Double? = null,
    val fibraPlato: Double? = null,
    val azucarPlato: Double? = null,
    val sodioPlato: Double? = null,
    val disponibilidadPlato: Boolean = true,
    val esVegetariano: Boolean = false,
    val esVegano: Boolean = false,
    val sinGluten: Boolean = false,
    val imagenUrl: String? = null
)

//@Serializable
//data class Plato(
//    val id: Int,
//    val idLugar:Int,
//    val idCategoria:Int,
//    val nombrePlato:String,
//    val descripcionPlato: String,
//    val precioPlato: Double,
//    val caloriasPlato: Int,
//    val protePlato:Double,
//    val carbPlato: Double,
//    val grasaPlato: Double,
//    val fibraPlato: Double,
//    val azucarPlato: Double,
//    val sodioPlato: Double,
//    val disponibilidadPlato: Boolean,
//    val esVegetariano: Boolean,
//    val esVegano: Boolean,
//    val sinGluten: Boolean,
//    val imagenUrl: String
//)
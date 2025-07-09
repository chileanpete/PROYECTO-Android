package com.example.proyecto_droid.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para Favorito Plato
 */
data class FavoritoPlato(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("fecha_agregado")
    val fechaAgregado: String? = null,
    
    @SerializedName("plato")
    val plato: Plato? = null
)

/**
 * Modelo para Menú Diario
 */
data class MenuDiario(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("fecha")
    val fecha: String,
    
    @SerializedName("tipo_comida")
    val tipoComida: String, // desayuno, almuerzo, cena, snack
    
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("cantidad")
    val cantidad: Double = 1.0,
    
    @SerializedName("unidad")
    val unidad: String = "porcion",
    
    @SerializedName("calorias_planificadas")
    val caloriasPlanificadas: Double? = null,
    
    @SerializedName("completado")
    val completado: Boolean = false,
    
    @SerializedName("fecha_completado")
    val fechaCompletado: String? = null,
    
    @SerializedName("notas")
    val notas: String? = null,
    
    @SerializedName("plato")
    val plato: Plato? = null
)

/**
 * Modelo para Categoría de Comida
 */
data class CategoriaComida(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String? = null,
    
    @SerializedName("activa")
    val activa: Boolean = true,
    
    @SerializedName("orden")
    val orden: Int = 0
)

/**
 * Modelo para Lugar de Comida
 */
data class LugarComida(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("direccion")
    val direccion: String? = null,
    
    @SerializedName("telefono")
    val telefono: String? = null,
    
    @SerializedName("horario_apertura")
    val horarioApertura: String? = null,
    
    @SerializedName("horario_cierre")
    val horarioCierre: String? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("latitud")
    val latitud: Double? = null,
    
    @SerializedName("longitud")
    val longitud: Double? = null
)

/**
 * Modelo para Tipo de Ejercicio
 */
data class TipoEjercicio(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String? = null,
    
    @SerializedName("calorias_por_minuto")
    val caloriasPorMinuto: Double? = null,
    
    @SerializedName("categoria")
    val categoria: String? = null, // cardio, fuerza, flexibilidad, etc.
    
    @SerializedName("nivel_dificultad")
    val nivelDificultad: String? = null, // principiante, intermedio, avanzado
    
    @SerializedName("activo")
    val activo: Boolean = true
) 
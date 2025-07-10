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
    @SerializedName("id_lugar")
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

/**
 * Modelo para Menú Favorito del Usuario
 */
data class MenuFavoritoUsuario(
    @SerializedName("id_menu_favorito")
    val id: Int = 0,
    
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("nombre_menu")
    val nombreMenu: String,
    
    @SerializedName("descripcion")
    val descripcion: String? = null,
    
    @SerializedName("tipo_comida")
    val tipoComida: String? = null, // desayuno, almuerzo, cena, snack
    
    @SerializedName("calorias_totales")
    val caloriasTotales: Double? = null,
    
    @SerializedName("activo")
    val activo: Boolean = true,
    
    @SerializedName("veces_usado")
    val vecesUsado: Int = 0,
    
    @SerializedName("fecha_ultimo_uso")
    val fechaUltimoUso: String? = null,
    
    @SerializedName("created_at")
    val fechaCreacion: String? = null,
    
    @SerializedName("platos")
    val platos: List<MenuFavoritoPlato> = emptyList()
)

/**
 * Modelo para Plato dentro de un Menú Favorito
 */
data class MenuFavoritoPlato(
    @SerializedName("id_menu_plato")
    val id: Int = 0,
    
    @SerializedName("id_menu_favorito")
    val idMenuFavorito: Int,
    
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("cantidad")
    val cantidad: Double = 1.0,
    
    @SerializedName("unidad")
    val unidad: String = "porcion",
    
    @SerializedName("calorias_porcion")
    val caloriasPorcion: Double? = null,
    
    @SerializedName("notas")
    val notas: String? = null,
    
    @SerializedName("orden")
    val orden: Int = 1,
    
    @SerializedName("plato")
    val plato: Plato? = null
)

/**
 * Request para crear un menú favorito
 */
data class CreateMenuFavoritoRequest(
    @SerializedName("id_usuario")
    val idUsuario: Int,
    
    @SerializedName("nombre_menu")
    val nombreMenu: String,
    
    @SerializedName("descripcion")
    val descripcion: String? = null,
    
    @SerializedName("tipo_comida")
    val tipoComida: String? = null,
    
    @SerializedName("platos")
    val platos: List<PlatoMenuFavoritoRequest>
)

/**
 * Datos de plato para crear menú favorito
 */
data class PlatoMenuFavoritoRequest(
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("cantidad")
    val cantidad: Double = 1.0,
    
    @SerializedName("unidad")
    val unidad: String = "porcion",
    
    @SerializedName("notas")
    val notas: String? = null
)

/**
 * Request para agregar un plato a un menú favorito existente
 */
data class AgregarPlatoMenuRequest(
    @SerializedName("id_plato")
    val idPlato: Int,
    
    @SerializedName("cantidad")
    val cantidad: Double = 1.0,
    
    @SerializedName("unidad")
    val unidad: String = "porcion",
    
    @SerializedName("notas")
    val notas: String? = null
)
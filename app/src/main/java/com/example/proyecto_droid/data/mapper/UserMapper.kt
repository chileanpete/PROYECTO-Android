package com.example.proyecto_droid.data.mapper

import com.example.proyecto_droid.data.model.User as DataUser
import com.example.proyecto_droid.domain.model.User as DomainUser

/**
 * Mapper para convertir entre modelos de datos y dominio
 */
object UserMapper {
    
    /**
     * Convierte de modelo de datos a modelo de dominio
     */
    fun DataUser.toDomain(): DomainUser {
        return DomainUser(
            id = this.id,
            email = this.email,
            nombre = this.nombre,
            apellidos = this.apellidos,
            fechaNacimiento = this.fechaNacimiento,
            genero = this.genero,
            alturaCm = this.alturaCm,
            pesoKg = this.pesoKg,
            nivelActividad = this.nivelActividad,
            objetivoPrincipal = this.objetivoPrincipal,
            preferenciasAlimentarias = this.preferenciasAlimentarias,
            alergias = this.alergias,
            createdAt = this.createdAt ?: "",
            updatedAt = this.updatedAt ?: ""
        )
    }
    
    /**
     * Convierte de modelo de dominio a modelo de datos
     */
    fun DomainUser.toData(): DataUser {
        return DataUser(
            id = this.id,
            email = this.email,
            nombre = this.nombre,
            apellidos = this.apellidos,
            fechaNacimiento = this.fechaNacimiento,
            genero = this.genero,
            alturaCm = this.alturaCm,
            pesoKg = this.pesoKg,
            nivelActividad = this.nivelActividad,
            objetivoPrincipal = this.objetivoPrincipal,
            preferenciasAlimentarias = this.preferenciasAlimentarias,
            alergias = this.alergias,
            passwordHash = "", // Se manejará por separado por seguridad
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
} 
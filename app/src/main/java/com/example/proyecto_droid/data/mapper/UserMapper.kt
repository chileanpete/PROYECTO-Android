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
            preferenciasAlimentarias = this.preferenciasAlimentarias ?: "",
            alergias = this.alergias ?: "",
            createdAt = this.fechaRegistro ?: "",
            updatedAt = this.fechaUltimoAcceso ?: ""
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
            preferenciasAlimentarias = this.preferenciasAlimentarias.takeIf { it.isNotBlank() },
            alergias = this.alergias.takeIf { it.isNotBlank() },
            passwordHash = "", // Se manejará por separado por seguridad
            fechaRegistro = this.createdAt.takeIf { it.isNotBlank() },
            fechaUltimoAcceso = this.updatedAt.takeIf { it.isNotBlank() }
        )
    }
} 
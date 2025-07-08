package com.example.proyecto_droid.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyecto_droid.data.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val passwordHash: String,
    val nombre: String,
    val apellidos: String,
    val fechaNacimiento: String,
    val genero: String,
    val alturaCm: Int,
    val pesoKg: Double,
    val nivelActividad: String,
    val objetivoPrincipal: String,
    val preferenciasAlimentarias: String?,
    val alergias: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long? = null
) {
    fun toUser(): User {
        return User(
            email = email,
            passwordHash = passwordHash,
            nombre = nombre,
            apellidos = apellidos,
            fechaNacimiento = fechaNacimiento,
            genero = genero,
            alturaCm = alturaCm,
            pesoKg = pesoKg,
            nivelActividad = nivelActividad,
            objetivoPrincipal = objetivoPrincipal,
            preferenciasAlimentarias = preferenciasAlimentarias ?: "",
            alergias = alergias ?: ""
        )
    }
    
    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                email = user.email,
                passwordHash = user.passwordHash,
                nombre = user.nombre,
                apellidos = user.apellidos,
                fechaNacimiento = user.fechaNacimiento,
                genero = user.genero,
                alturaCm = user.alturaCm,
                pesoKg = user.pesoKg,
                nivelActividad = user.nivelActividad,
                objetivoPrincipal = user.objetivoPrincipal,
                preferenciasAlimentarias = user.preferenciasAlimentarias.ifEmpty { null },
                alergias = user.alergias.ifEmpty { null }
            )
        }
    }
} 
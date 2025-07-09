package com.example.proyecto_droid.data.remote

import com.example.proyecto_droid.data.model.LoginRequest
import com.example.proyecto_droid.data.model.User

object LaravelTestData {
    
    // Usuarios existentes en la base de datos de Laravel (según UsuarioSeeder)
    val EXISTING_USERS = listOf(
        LoginRequest(
            email = "juan@example.com",
            password = "password123"
        ),
        LoginRequest(
            email = "maria@example.com", 
            password = "password123"
        ),
        LoginRequest(
            email = "carlos@example.com",
            password = "password123"
        )
    )
    
    // Datos para crear nuevos usuarios de prueba
    fun createTestUser(email: String): User {
        return User(
            email = email,
            passwordHash = "password123",
            nombre = "Usuario",
            apellidos = "Test",
            fechaNacimiento = "1990-01-01",
            genero = "M",
            alturaCm = 170,
            pesoKg = 70.0,
            nivelActividad = "moderado",
            objetivoPrincipal = "mantener_peso",
            preferenciasAlimentarias = "Sin restricciones",
            alergias = "Ninguna"
        )
    }
    
    fun createTestUserWithTimestamp(): User {
        return createTestUser("test_${System.currentTimeMillis()}@example.com")
    }
    
    // Datos de ejemplo para diferentes tipos de usuarios
    fun createActiveUser(): User {
        return User(
            email = "activo_${System.currentTimeMillis()}@example.com",
            passwordHash = "password123",
            nombre = "Usuario",
            apellidos = "Activo",
            fechaNacimiento = "1995-05-15",
            genero = "M",
            alturaCm = 175,
            pesoKg = 70.5,
            nivelActividad = "activo",
            objetivoPrincipal = "perder_peso",
            preferenciasAlimentarias = "Vegetariano",
            alergias = "Ninguna"
        )
    }
    
    fun createSedentaryUser(): User {
        return User(
            email = "sedentario_${System.currentTimeMillis()}@example.com",
            passwordHash = "password123",
            nombre = "Usuario",
            apellidos = "Sedentario",
            fechaNacimiento = "1988-12-10",
            genero = "M",
            alturaCm = 180,
            pesoKg = 85.0,
            nivelActividad = "sedentario",
            objetivoPrincipal = "ganar_musculo",
            preferenciasAlimentarias = "Alto en proteínas",
            alergias = "Ninguna"
        )
    }
} 
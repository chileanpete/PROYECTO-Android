package com.example.proyecto_droid.model

data class ApiResponse<T>(
    val success: Boolean,
    val data: T
) 